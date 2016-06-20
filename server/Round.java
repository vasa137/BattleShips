package battleships.server;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import battleships.communication.CONFIRM_DEPLOY;
import battleships.communication.CommunicationCommands;
import battleships.communication.DEPLOY_SHIPS;
import battleships.communication.FIRE;
import battleships.communication.QUIT;
import battleships.communication.STATE;

/*
 * u stanju round, igra šalje svim igračima poruku ROUND x time [name1; name2; ...; namen]
	gde x označava redni broj poteza (runde), time označava broj sekundi predviđenih za
	trajanje poteza, a namex označava ime x-tog igrača koji može da učestvuje u ovom potezu.
	Nakon slanja poruke, igra čeka vreme trajanja poteza tokom kojeg može od igrača da primi
	poruku:
 */

public class Round extends GameState {
	private static Round instance;
	private int n = 0; // round number
	ArrayList<Player> activePlayers; // list of active players, spectators are in list in server
	Set <String> set = new HashSet<String>(); // set thats stores fire from currentround
	
	public Set<String> getSet(){
		return set;
	}
	
	//Commands allowed in FIRE STATE(each round)
	public Round(){
		commandMap.put(CommunicationCommands.STATE_REQUEST, new STATE());
		commandMap.put(CommunicationCommands.QUIT_MESSAGE, new QUIT());
	}
	
	public static Round getInstance(){
		if(instance == null){
			instance = new Round();
		}
		return instance;
	}
	
	public void initialize(ArrayList<Player> activePlayers){
		n = 0;
		this.activePlayers = activePlayers;
	}
	
	@Override
	public void execute(BattleOverseer battleOverseer) throws InterruptedException {
		set.clear(); // remove all bullets from previous round
		n++; // round number
		String messageForAllPlayers = CommunicationCommands.ROUND + " " + n + " " + battleOverseer.myGame.getRoundTime() + " [";
		System.out.println("Phase : ROUND " + n);
		
		synchronized(this){
		
			for(int i = 0; i < activePlayers.size() ; i++){
				messageForAllPlayers += activePlayers.get(i).getName() + ";";
			}
		
		}
		
		messageForAllPlayers += "]";
		
		synchronized(this){
			battleOverseer.myGame.sendMessageToAllPlayers(messageForAllPlayers);
		}
		
		timer = new Timer(this,battleOverseer.myGame.getRoundTime()); // wait till round time exceeds
	
		commandMap.put(CommunicationCommands.FIRE, new FIRE());
		
		synchronized(this){
			wait();
		}
		
		commandMap.remove(CommunicationCommands.FIRE, new FIRE());
	
	}


	@Override
	public synchronized GameState setNextState() {
		if(activePlayers.size() < 2){
			try {
				Game.instance().stopGame();
			} catch (SocketException e) {}
			return WFP.getInstance();
		}
		else return UpdateShips.getInstance();
	}

	@Override
	public synchronized String stateDescription() {
		if(timer != null) return CommunicationCommands.R + " " + n + " " + timer.getTimeLeft(); // x time
		else{
			try {
				return CommunicationCommands.R + " " + n + " " + Game.instance().getRoundTime(); // x time
			} catch (SocketException e) {
				System.out.println("Exception");
			};
		}
		return null;

	}

	public ArrayList<Player> getActivePlayers() {
		return activePlayers;
	}

	
}
