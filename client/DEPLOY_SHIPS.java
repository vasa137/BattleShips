package battleships.client;

import battleships.communication.CommunicationCommands;

public class DEPLOY_SHIPS extends Command {
	
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
			System.out.println("SHIP DEPLOY STATE STARTED!\n");
	     	// at the beggining confirm deploy 
			player.send(CommunicationCommands.CONFIRM_DEPLOY + " " + player.getID()+ " " + message);
			// and send state reques to update left time
			player.send(CommunicationCommands.STATE_REQUEST);
			player.setState(Menu.DEPLOY_SHIPS_STATE);
			player.setDeployContent(message.substring(message.indexOf(" ") + 1));
  }
}