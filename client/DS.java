package battleships.client;

import battleships.communication.CommunicationCommands;

public class DS extends Command {
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String x = message.substring(message.indexOf(" ") + 1);
		// set end time left 
		
		player.setEndTime(Integer.parseInt(x)+System.currentTimeMillis());
	
			//System.out.println("TIME LEFT FOR DEPLOY : "+ Integer.parseInt(x)/1000 );
		((DeployPanel)player.frame.getPanel(MyFrame.DEPLOY_SHIPS_STATE)).setTimeLabel(""+ (int)(Integer.parseInt(x)/1000.));
		 player.frame.setTime();
		 player.send(CommunicationCommands.STATE_REQUEST);
	}
}