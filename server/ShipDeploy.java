package battleships.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import battleships.communication.CONFIRM_DEPLOY;
import battleships.communication.CommunicationCommands;
import battleships.communication.DEPLOY_SHIPS;
import battleships.communication.QUIT;
import battleships.communication.SERVER_JOIN;
import battleships.communication.STATE;

public class ShipDeploy extends GameState {
	private static ShipDeploy instance;
	private final long timeForResponse = 5000;
	private final long timeForRandomLayout = 10000;
	private volatile boolean [] confirmedPlayers;
	
	private ShipDeploy(){
		commandMap.put(CommunicationCommands.CONFIRM_DEPLOY, new CONFIRM_DEPLOY());
		commandMap.put(CommunicationCommands.QUIT_MESSAGE, new QUIT());
		commandMap.put(CommunicationCommands.SHIP_LAYOUT, new DEPLOY_SHIPS());
		commandMap.put(CommunicationCommands.STATE_REQUEST, new STATE());
	}
	
	public static ShipDeploy getInstance(){
		if(instance == null){
			instance = new ShipDeploy();
		}
		return instance;
	}
	
	@Override
	public void execute(BattleOverseer battleOverseer) throws InterruptedException, IOException {
		System.out.println("Usao u SHIP Deploy");
		
		String messageForAllPlayers = CommunicationCommands.DEPLOY_SHIPS + " D" + "=" + battleOverseer.myGame.getTableSize() + ";";
		String shipLayout = "D" + "=" + battleOverseer.myGame.getTableSize() + ";";
		
		//information for all clients
		for(int i = 0; i < battleOverseer.myGame.shipCount.length; i++){
			if(battleOverseer.myGame.shipCount[i] > 0){
				String toAdd = "S(" + i + ")="+ battleOverseer.myGame.shipCount[i] + ";";
				shipLayout += toAdd; messageForAllPlayers += toAdd;
			}
		}
	
		
		ArrayList<Player> players = battleOverseer.myGame.getPlayers();
		
		int potentialNumberOfPlayers = players.size();
		System.out.println("POT : " + potentialNumberOfPlayers);
		
		confirmedPlayers = new boolean[potentialNumberOfPlayers];
	
		battleOverseer.myGame.sendMessageToAllPlayers(messageForAllPlayers);
		
		String messageForNotConfirmed = messageForAllPlayers;
		int counter = 0;
		//repeating confirmation messages
		for(int j = 0; j < 3; j++){
			Thread.sleep(timeForResponse);
			counter = 0;
			for(int i = 0; i < potentialNumberOfPlayers; i++){
				if(confirmedPlayers[i] == false){
					Player player = players.get(i);
					player.playerProxy.send(messageForNotConfirmed);
				}	
				else counter ++;
			}
			if(counter == potentialNumberOfPlayers) break;
		}
		
		if( counter != potentialNumberOfPlayers) Thread.sleep(timeForResponse);
		
		counter = 0;
		//because arrayList shifts
		for(int i = 0; i < potentialNumberOfPlayers; i++){
			if(confirmedPlayers[i] == false){
				players.remove(i - counter);
				counter++;
			}		
		}
		
		
		timer = new Timer(this,battleOverseer.myGame.getDeployTime());

		synchronized(this) { 
			wait(); 
		}
		
		
		for(int i = 0; i < players.size(); i++){
			if(!(players.get(i).layoutAcc())){
				players.get(i).reportMessage(CommunicationCommands.RANDOM_LAYOUT);
				Thread.sleep(timeForRandomLayout);
				//players.get(i).getTable().randomLayout(shipLayout);
			}
		}
		
		counter = 0;
		for(int i = 0; i < players.size(); i++){
			if(!(players.get(i).layoutAcc())){
				players.remove(i - counter);
				counter++;
			}		
		}
		
		Round.getInstance().initialize(battleOverseer.myGame);
	}

	@Override
	public synchronized Round setNextState() {
		try {
			Round.getInstance().initialize(Game.instance());
		} catch (SocketException e) {}
		return Round.getInstance();
	}

	@Override
	public synchronized String stateDescription() {
		if(timer != null) return CommunicationCommands.DS + " " + timer.getTimeLeft();
		else{
			try {
				return CommunicationCommands.DS + " " + Game.instance().getDeployTime();
			} catch (SocketException e) {
				System.out.println("Exception");
			};
		}
		return null;
	}
	public boolean [] getConfirmedPlayers() {
		return confirmedPlayers;
	}

}
