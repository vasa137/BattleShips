package battleships.server;

import java.net.SocketException;

import battleships.communication.CommunicationCommands;
import battleships.communication.QUIT;
import battleships.communication.SERVER_JOIN;
import battleships.communication.STATE;

// WAITING FOR PLAYERS STATE 

public class WFP extends GameState {
	private static WFP instance;
	
	//commands allowed in Waiting For Players State
	private WFP(){
		commandMap.put(CommunicationCommands.JOIN_MESSAGE, new SERVER_JOIN());
		commandMap.put(CommunicationCommands.STATE_REQUEST, new STATE());
		commandMap.put(CommunicationCommands.QUIT_MESSAGE, new QUIT());
		
	}
	
	public static WFP getInstance(){
		if(instance == null){
			instance = new WFP();
		}
		return instance;
	}
	

	@Override
	public synchronized ShipDeploy setNextState() {
		return ShipDeploy.getInstance();
	}

	@Override
	public synchronized String stateDescription() throws SocketException {
		return "WFP " + Game.instance().players.size() + "/" + Game.instance().getMaxPlayers();
	}


	@Override
	public synchronized void execute(BattleOverseer battleOverseer) throws InterruptedException {}
}
