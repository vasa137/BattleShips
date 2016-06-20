package battleships.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Set;

import battleships.communication.CommunicationCommands;
import battleships.common.Bad_Coordinate;
import battleships.common.Coordinate;
import battleships.common.StringSpliter;
import battleships.communication.QUIT;
import battleships.communication.STATE;

public class UpdateShips extends GameState {
	private static UpdateShips instance;
	private final int sleepTime = 5000; // time to wait after update
	
	//Commands allowed in this state
	public UpdateShips(){
		commandMap.put(CommunicationCommands.STATE_REQUEST, new STATE());
		commandMap.put(CommunicationCommands.QUIT_MESSAGE, new QUIT());
	}
	
	public static UpdateShips getInstance(){
		if(instance == null){
			instance = new UpdateShips();
		}
		return instance;
	}

	@Override
	public void execute(BattleOverseer battleOverseer) throws InterruptedException {
		Set<String> set = Round.getInstance().getSet();
		StringBuilder update = new StringBuilder(CommunicationCommands.UPDATE + " [");
		String [] fires = new String[set.size()];
		set.toArray(fires);
		
		System.out.println("Phase : UPDATE");
		
	
			for(int i = 0 ; i < fires.length; i++){
				String name = StringSpliter.delimStr(fires[i], "{}")[0];
				Coordinate coord = Coordinate.makeCoordinate(StringSpliter.delimStr(fires[i], "{}")[1]);
				Player player = null;
				synchronized(this){
					player = battleOverseer.myGame.gameServer.getNameMap().get(name);
				}
				
				if(player != null){
					update.append(fires[i]);
					try {
						if( player.getTable().hitTable(coord)){
							update.append("H;"); //hit
						}
						else update.append("M;"); //miss
					} catch (Bad_Coordinate e) {}		
				}
			}
		
		update.append("]");
		
		battleOverseer.myGame.sendMessageToAllPlayers(update.toString()); // send update to all clients
		
		ArrayList<Player> activePlayers = Round.getInstance().activePlayers; // get active players from previous round
		
		int counter = 0;
		boolean gameStillRunning = false;
		synchronized(this){
			for(int i = 0; i < activePlayers.size() ; i++){
				Player player = activePlayers.get(i);
				if(player.getTable().numberOfOperativeSegments() == 0){ // all ships destroyed
					activePlayers.remove(i- counter);
					counter ++ ;
					try {
						player.playerProxy.send(CommunicationCommands.GAME_OVER); 
					} catch (IOException e) {
	
					}
				}
			}
		
			if(activePlayers.size() == 1){ // if only one player left, he is automatically winner
				Player winner = activePlayers.get(0);
				
				activePlayers.remove(0);
				
				activePlayers.remove(winner);
				
				winner.reportMessage(CommunicationCommands.VICTORY); //send him victory message
				battleOverseer.myGame.sendMessageToAllPlayers(CommunicationCommands.GAME_WON + " " + winner.getName()); //messageForAll- game winner is..
			}
			else if(activePlayers.size() == 0){ // if result is tied
				battleOverseer.myGame.sendMessageToAllPlayers(CommunicationCommands.NO_VICTORY);
			}
			else{ // game still running
				gameStillRunning = true;
			}
		}
		
		if(gameStillRunning) Thread.sleep(sleepTime);	
	}

	@Override
	public synchronized GameState setNextState() {
		if(Round.getInstance().activePlayers.size() < 2){ // if game is finished go back to waiting for players STATE
			try {
				Game.instance().stopGame();
			} catch (SocketException e) {}
			return WFP.getInstance();
		}
		else return Round.getInstance(); // go to next round
	}

	@Override
	public String stateDescription() {
		return CommunicationCommands.UPDATE;
	}



}
