package battleships.communication;

import java.io.IOException;

import battleships.server.Player;


//state executes in server too
public class STATE extends ClientCommand {
	public void executeClientMessage(Server server,String message,String cmd)throws IOException{
		PlayerProxy pp = new PlayerProxy(server, server.receivePacket.getAddress(), server.receivePacket.getPort());
		//if we don't have this address in our map
		if(server.connectedPlayers.containsKey(pp)){
			pp.send(CommunicationCommands.ACCESS_DENIED);
		}
		else pp.send(server.game.getState().stateDescription()); // send state description to client
	}

	@Override
	public void executeServerToPlayerMessage(Server server, Player player,  String message) {} // empty body because it will never execute
}
