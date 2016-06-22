package battleships.client;

import battleships.communication.CommunicationCommands;

public class WELCOME extends Command {


	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String delims = " ";
		String[] tokens = message.split(delims);
		player.setID(Integer.parseInt(tokens[1]));
		System.out.println("WELCOME");
		// set flag that tells us that our player is in game
		player.setState(Menu.OBSERVE_STATE);
		((InfoPanel)player.frame.getPanel(MyFrame.INFO_STATE)).setInfoMessage("WELCOME! ");
		player.frame.changePanel(MyFrame.INFO_STATE);
		player.send(CommunicationCommands.STATE_REQUEST);
	}
	
}
