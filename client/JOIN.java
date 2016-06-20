package battleships.client;

import battleships.communication.CommunicationCommands;

public class JOIN extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		System.out.print("Enter name:");
		String name=Citaj.String();
		player.setName(name);
		player.send(CommunicationCommands.JOIN_MESSAGE + " " + player.getName());
	}
	
}
