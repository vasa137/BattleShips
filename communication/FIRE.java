package battleships.communication;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import battleships.common.Coordinate;
import battleships.common.StringSpliter;
import battleships.communication.ClientCommand;
import battleships.communication.Server;
import battleships.server.Player;
import battleships.server.Round;

public class FIRE extends ClientCommand{

	@Override
	public void executeClientMessage(Server server, String message, String cmd) throws IOException {
		forwardMessage(server, cmd + " " + message);	// only forwarding
	}

	@Override
	public void executeServerToPlayerMessage(Server server, Player player, String message) {
		System.out.println("FIRE : " + message);
		if(!(Round.getInstance().getActivePlayers().contains(player))){
			player.reportMessage(CommunicationCommands.ACCESS_DENIED);
			return;
		}
		String delimiters = "[];";
		String [] tokens = StringSpliter.delimStr(message, delimiters);
		
		//player can only fire n times, n = number of operative segments of his ships
		if(tokens.length > player.getTable().numberOfOperativeSegments()){
			player.reportMessage(CommunicationCommands.FIRE_REJECTED);
			return;
		}
		
		Set<String> temporarySet = new HashSet<String>(); // register fire in temporary set
		
		for(int i = 0; i < tokens.length; i ++){
			System.out.println(tokens[i]);
			System.out.println(StringSpliter.delimStr(tokens[i],"{}")[0]);
			Coordinate coord = Coordinate.makeCoordinate(StringSpliter.delimStr(tokens[i],"{}")[1]);
			if(!(coord.inRange(server.game.getTableSize()))){
				player.reportMessage(CommunicationCommands.FIRE_REJECTED); // forgot all previous hits
				return;
			}
			temporarySet.add(tokens[i]); // add a fire to the temporary set
		}
		Round.getInstance().getSet().addAll(temporarySet);  // if all bullets are okay, union temporary set with real set, that is stored in Round class
		
	}
	
}
