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
	private ArrayList<Player> activePlayers;
	
	private ShipDeploy(){
		commandMap.put(CommunicationCommands.QUIT_MESSAGE, new QUIT());
		commandMap.put(CommunicationCommands.STATE_REQUEST, new STATE());
	}
	
	public static ShipDeploy getInstance(){
		if(instance == null){
			instance = new ShipDeploy();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(BattleOverseer battleOverseer) throws InterruptedException, IOException {
		System.out.println("Phase : SHIP Deploy");
		
		String messageForAllPlayers = CommunicationCommands.DEPLOY_SHIPS + " D" + "=" + battleOverseer.myGame.getTableSize() + ";";
		//String shipLayout = "D" + "=" + battleOverseer.myGame.getTableSize() + ";";
		
		//information for all clients
		for(int i = 0; i < battleOverseer.myGame.shipCount.length; i++){
			if(battleOverseer.myGame.shipCount[i] > 0){
				String toAdd = "S(" + i + ")="+ battleOverseer.myGame.shipCount[i] + ";";
				//shipLayout += toAdd; 
				messageForAllPlayers += toAdd;
			}
		}
	
		int potentialNumberOfPlayers = 0;
		ArrayList<Player> players = null;
		
		synchronized(this){
		
			players = battleOverseer.myGame.getPlayers();
			activePlayers = battleOverseer.myGame.clonePlayersToActive();
			
	
			potentialNumberOfPlayers = players.size();
		
			confirmedPlayers = new boolean[potentialNumberOfPlayers];
		
			battleOverseer.myGame.sendMessageToAllPlayers(messageForAllPlayers);
		
		}
		
		commandMap.put(CommunicationCommands.CONFIRM_DEPLOY, new CONFIRM_DEPLOY());
		
		String messageForNotConfirmed = messageForAllPlayers;
		int counter = 0;
		//repeating confirmation messages
		
		for(int j = 0; j < 3; j++){
			Thread.sleep(timeForResponse);
			counter = 0;
			try{
				
				synchronized(this){
				
					for(int i = 0; i < potentialNumberOfPlayers; i++){
						
							if(confirmedPlayers[i] == false){
			
								Player player = players.get(i);
								player.playerProxy.send(messageForNotConfirmed);
							}	
							else counter ++;
					}
				
				}
			}
			catch(IndexOutOfBoundsException e) {
				synchronized(this){
					for(int i = players.size(); i <  potentialNumberOfPlayers ; i++){
						if(confirmedPlayers[i] == false){
							counter++;
						}
					}
				}
			}
			if(counter == potentialNumberOfPlayers) break;
		}
		
		if( counter != potentialNumberOfPlayers) Thread.sleep(timeForResponse);
		
		commandMap.remove(CommunicationCommands.CONFIRM_DEPLOY); // no more confirms
		
		counter = 0;
		
		//because arrayList shifts
		synchronized(this){
			for(int i = 0; i < potentialNumberOfPlayers; i++){
				if(confirmedPlayers[i] == false){
					activePlayers.remove(i - counter);
					counter++;
				}		
			}
		}
		
		timer = new Timer(this,battleOverseer.myGame.getDeployTime());
		
		commandMap.put(CommunicationCommands.SHIP_LAYOUT, new DEPLOY_SHIPS());

		synchronized(this) { 
			wait(); 
		}
		
		commandMap.remove(CommunicationCommands.SHIP_LAYOUT);
		
		synchronized(this){
			for(int i = 0; i < activePlayers.size(); i++){
				if(!(activePlayers.get(i).layoutAcc())){
					activePlayers.get(i).reportMessage(CommunicationCommands.RANDOM_LAYOUT);
					Thread.sleep(timeForRandomLayout);
					//players.get(i).getTable().randomLayout(shipLayout);
				}
			}
		}
		
		counter = 0;
		
		synchronized(this){
			for(int i = 0; i < activePlayers.size(); i++){
				if(!(activePlayers.get(i).layoutAcc())){
					activePlayers.remove(i - counter);
					counter++;
				}		
			}
			
			if(activePlayers.size() >= 2){		
				Round.getInstance().initialize(activePlayers);
			}
		}
	}

	@Override
	public synchronized GameState setNextState() {
		if(activePlayers.size() < 2){
			try {
				Game.instance().stopGame();
			} catch (SocketException e) {}
			return WFP.getInstance();
		}
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

	public ArrayList<Player> getActivePlayers() {
		return activePlayers;
	}

}
