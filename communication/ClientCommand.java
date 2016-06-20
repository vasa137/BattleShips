package battleships.communication;

import java.io.IOException;

import battleships.server.Player;

public abstract class ClientCommand {
	 public abstract void executeClientMessage(Server server, String message, String msg) throws IOException;
	 public abstract void executeServerToPlayerMessage(Server server, Player player, String message) throws IOException;
	 
	 //ID CHECK, is message valid?
	 protected void IDaccesDenied(Server server) throws IOException{
		 PlayerProxy pp = new PlayerProxy(server, server.receivePacket.getAddress(), server.receivePacket.getPort());
		 pp.send(CommunicationCommands.ACCESS_DENIED);
	 }
	 
	 //forward message to server.Player, because he will deal with in other thread, and server can focus on receiving messages from other clients
	 protected void forwardMessage(Server server,String message) throws IOException{
		String [] tokens = message.split(" ");
		int id = Integer.parseInt(tokens[1]);
		if(server.connectedPlayers.get(id) == null){
				IDaccesDenied(server);
				return;
		}
		PlayerProxy pp = server.connectedPlayers.get(id);
		pp.receivedMessage(message); // forward
	 }
}
