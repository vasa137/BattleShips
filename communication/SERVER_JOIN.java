package battleships.communication;

import battleships.server.Game;
import battleships.server.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.Hashtable;

//server do not forward JOIN and STATE, and forward all other commands

public class SERVER_JOIN extends ClientCommand {
	 public void executeClientMessage(Server server, String message, String cmd) throws IOException{
		 String name = "",password ="";
		 //parse
		if(message.contains("/")){
		 name = message.substring(0, message.indexOf("/") - 1);
		 password = message.substring(message.indexOf("/") + 1);
		}
		else name = message;
		
		 PlayerProxy pp = new PlayerProxy(server, server.receivePacket.getAddress(), server.receivePacket.getPort());
		 //if player with same name exists
		 if(server.nameMap.get(name) != null) {
			
			 pp.send(CommunicationCommands.DUPLICATE_NAME);
			 return;
		 }
		 
		 // if message is JOIN name
		 if(password.equals("")){ 
	         if(!server.game.getPassword().equals("")){
	        	 pp.send(CommunicationCommands.PASSWORD_REQUIRED);
	        	 return;
	         }    
		 }
		 else// if message is JOIN name /password
			 if(!password.equals(server.game.getPassword())){
				 pp.send(CommunicationCommands.ACCESS_DENIED);
				 return;
			 }
		 
	 	
         server.connectedPlayers.put(++server.clientID, pp);
         Player player = server.game.newPlayer(pp, name, server.clientID);
         server.nameMap.put(name,player);
         server.playersMap.put(server.clientID,player);
    	 System.out.println("Added new player: " + name);
    	 pp.send(CommunicationCommands.WELCOME_MESSAGE + " " + server.clientID);
		 
	 }

	@Override
	public void executeServerToPlayerMessage(Server server, Player player, String message) {}

}
