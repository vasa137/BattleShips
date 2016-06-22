package battleships.client;

import battleships.communication.CommunicationCommands;

public class R extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		
			String [] tokens=message.split(" ");
			//split tokens to get Round tim left
			int x=Integer.parseInt(tokens[2]);
			
			player.setEndTime(x+System.currentTimeMillis());
			
			((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).setTimeLabel(""+ (int)(x/1000));
			player.frame.setTime();
			player.send(CommunicationCommands.STATE_REQUEST);
					
	}
}