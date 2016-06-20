package battleships.client;

import battleships.communication.CommunicationCommands;

public class PASSWORD_REQUIRED extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
			System.out.print("Enter password:");
			String password=Citaj.String();
			player.send(CommunicationCommands.JOIN_MESSAGE + " " + player.getName()+ " /"+ password );
		}
	}
}
