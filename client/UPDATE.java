package battleships.client;

import javax.swing.JLabel;

import battleships.common.Coordinate;
import battleships.common.StringSpliter;

public class UPDATE extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message){
		String delims = " ={}[];";
		String[] tokens = StringSpliter.delimStr(message, delims);
		synchronized(player){
		System.out.println(message);
		}
		// after update exceeds in observe state
		for(int i=1; i <tokens.length;i=i+2){
			Coordinate coord= Coordinate.makeCoordinate(tokens[i+1].substring(0,4));
			player.update(tokens[i],coord,tokens[i+1].substring(4,5));
			
		}
		((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).setOpperativeSegments();
	}
}
