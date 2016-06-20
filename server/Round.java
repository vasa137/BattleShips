package battleships.server;

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
		commandMap.put(CommunicationCommands.FIRE, new FIRE());
	}
	
	public static Round getInstance(){
		if(instance == null){
			instance = new Round();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void initialize(Game game){
		n = 0;
		activePlayers = (ArrayList<Player>) game.getPlayers().clone();
	}
	
	@Override
	public synchronized void execute(BattleOverseer battleOverseer) throws InterruptedException {
		set.clear(); // remove all bullets from previous round
		n++; // round number
		String messageForAllPlayers = CommunicationCommands.ROUND + " " + n + " " + battleOverseer.myGame.getRoundTime() + " [";
		
		
		for(int i = 0; i < activePlayers.size() ; i++){
			messageForAllPlayers += activePlayers.get(i).getName() + ";";
		}
		
		messageForAllPlayers += "]";
		
		battleOverseer.myGame.sendMessageToAllPlayers(messageForAllPlayers);
		
		timer = new Timer(this,battleOverseer.myGame.getRoundTime()); // wait till round time exceeds
	
			wait();
	
	}


	@Override
	public synchronized UpdateShips setNextState() {
		return UpdateShips.getInstance();
	}

	@Override
	public synchronized String stateDescription() {
		return "R " + n + " " + timer.getTimeLeft(); // x time
	}

	
}
