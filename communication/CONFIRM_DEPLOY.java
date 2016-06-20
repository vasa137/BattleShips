package battleships.communication;

import java.io.IOException;

import battleships.communication.ClientCommand;
import battleships.communication.Server;
import battleships.server.Game;
import battleships.server.Player;
import battleships.server.ShipDeploy;

public class CONFIRM_DEPLOY extends ClientCommand {

	@Override
	public void executeClientMessage(Server server, String message,String cmd) throws IOException {
		forwardMessage(server,cmd + " " + message); // only forwarding
	}

	@Override
	public void executeServerToPlayerMessage(Server server, Player player, String message) throws IOException {
		int id = player.getID();
		int index = Game.instance().getPlayers().indexOf(player); // doradi, vraca -1
		System.out.println("index: " + index + " id : " + id);
		
		if(index != -1 ){
			ShipDeploy.getInstance().getConfirmedPlayers()[index] = true; 
			player.allocateTable();
			}// if deploy is confirmed set flag for player with index i
		}
}

