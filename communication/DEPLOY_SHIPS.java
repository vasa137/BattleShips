package battleships.communication;

import java.io.IOException;

import battleships.common.Bad_Coordinate;
import battleships.common.Ship;
import battleships.server.Game;
import battleships.server.Player;

public class DEPLOY_SHIPS extends ClientCommand {

	@Override
	public void executeClientMessage(Server server, String message, String cmd) throws IOException {
		forwardMessage(server, cmd + " " + message); // only forwarding
	}

	//message execution in server.Player
	@Override
	public void executeServerToPlayerMessage(Server server, Player player,  String message)
			throws IOException {
		if(player.layoutAcc()){ // layout can be only one time accepted
			player.reportMessage(CommunicationCommands.LAYOUT_REJECTED);
			return;
		}
		String [] tokens = message.split(";");
		player.makeTable(Game.instance().getTableSize()); // initialization of the table
		for(int i = 0; i < tokens.length; i++){
			Ship ship = Ship.makeShip(tokens[i]);
			try {
				if(!(player.getTable().putShip(ship))){ // tries to put ship on table
					player.reportMessage(CommunicationCommands.LAYOUT_REJECTED); 
					player.getTable().deleteTable(); //restaurate table because of layout rejection
					return;
				}
			} catch (Bad_Coordinate e) {
				player.reportMessage(CommunicationCommands.LAYOUT_REJECTED);
				player.getTable().deleteTable();
				return;
			}
		}
		player.reportMessage(CommunicationCommands.LAYOUT_ACCEPTED);
		player.setLayoutAcc(); // set layout accept flag
	}

}
