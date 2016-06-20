package battleships.communication;

import java.io.IOException;
import java.util.ArrayList;

import battleships.communication.ClientCommand;
import battleships.communication.Server;
import battleships.server.Player;

public class QUIT extends ClientCommand{
	public void executeClientMessage(Server server,String message, String cmd) throws IOException  {
		forwardMessage(server, cmd + " " + message); // only forwarding
	}
	
	//delete from all lists, maps etc.
	public void executeServerToPlayerMessage(Server server, Player player, String message) throws IOException{
		server.nameMap.remove(server.playersMap.get(player.getID()).getName());
		server.connectedPlayers.remove(player.getID());
		ArrayList<Player> players = server.game.getPlayers();
		Player playerToDelete = server.playersMap.get(player.getID());
		players.remove(playerToDelete);
		server.playersMap.remove(playerToDelete.getName());
		player.reportMessage(CommunicationCommands.BYE); // return BYE message on which client mustn't react
	}

}
