package battleships.client;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import battleships.client.BattleshipsPlayer.Receiver;
import battleships.communication.Client;
import battleships.communication.CommunicationCommands;

public class JOIN extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		System.out.print("Enter server IP address: ");
		String address=Citaj.String();
		System.out.print("Enter your name: ");
    	String name=Citaj.String();
    	
   
				try {
					player.client = new Client(InetAddress.getByName(address),name);
				} catch (SocketException | UnknownHostException e) {
					System.out.println("Unknown Host");
					return;
				}
			
        player.clientReciever =player.new Receiver();
		//start Reciever thread
        player.clientReciever.start();
        player.setName(name);
        player.send(CommunicationCommands.JOIN_MESSAGE + " " + name);
	}
	
}
