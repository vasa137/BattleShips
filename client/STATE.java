package battleships.client;

import battleships.communication.CommunicationCommands;

public class STATE extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		player.send(CommunicationCommands.STATE_REQUEST);
	}
	
}

