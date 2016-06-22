package battleships.client;

import battleships.communication.CommunicationCommands;

public class WFP extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String x = message.substring(message.indexOf(" ") + 1);
		// just announce state of number of applied players
		
		((InfoPanel)player.frame.getPanel(MyFrame.INFO_STATE)).setInfoMessage("WAITING FOR PLAYERS " + x +"!" );
		player.frame.changePanel(MyFrame.INFO_STATE);
		player.send(CommunicationCommands.STATE_REQUEST);
	
	}

}