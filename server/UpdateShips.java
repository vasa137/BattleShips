package battleships.server;

import java.io.IOException;
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
	private final int sleepTime = 3000; // time to wait after update
	
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
		
		for(int i = 0 ; i < fires.length; i++){
			String name = StringSpliter.delimStr(fires[i], "{}")[0];
			Coordinate coord = Coordinate.makeCoordinate(fires[i].split("{}")[1]);
			Player player = battleOverseer.myGame.gameServer.getNameMap().get(name);
			update.append(fires[i]);
			try {
				if( player.getTable().hitTable(coord)){
					update.append("H;"); //hit
				}
				else update.append("M;"); //miss
			} catch (Bad_Coordinate e) {}
		}
		
		battleOverseer.myGame.sendMessageToAllPlayers(update.toString()); // send update to all clients
		
		ArrayList<Player> players = Round.getInstance().activePlayers; // get active players from previous round
		
		for(int i = 0; i < players.size() ; i++){
			Player player = players.get(i);
			if(player.getTable().numberOfOperativeSegments() == 0){ // all ships destroyed
				players.remove(i);
				try {
					player.playerProxy.send(CommunicationCommands.GAME_OVER); 
				} catch (IOException e) {

				}
			}
		}
		
		if(players.size() == 1){ // if only one player left, he is automatically winner
			Player winner = players.get(0);
			
			players.remove(0);
			
			players.remove(winner);
			
			winner.reportMessage(CommunicationCommands.VICTORY); //send him victory message
			battleOverseer.myGame.sendMessageToAllPlayers(CommunicationCommands.GAME_WON + " " + winner.getName()); //messageForAll- game winner is..
			battleOverseer.myGame.stopGame();
		}
		else if(players.size() == 0){ // if result is tied
			battleOverseer.myGame.sendMessageToAllPlayers(CommunicationCommands.NO_VICTORY);
			battleOverseer.myGame.stopGame();
		}
		else{ // game still running
			timer = new Timer(this,sleepTime);
			
		
				wait();
			
		}
	}

	@Override
	public synchronized GameState setNextState() {
		if(Round.getInstance().activePlayers.size() < 2){ // if game is finished go back to waiting for players STATE
			return WFP.getInstance();
		}
		else return Round.getInstance(); // go to next round
	}

	@Override
	public String stateDescription() {
		return "U";
	}



}
