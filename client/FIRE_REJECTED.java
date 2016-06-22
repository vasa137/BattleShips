package battleships.client;

import battleships.common.Coordinate;
import battleships.common.StringSpliter;

public class FIRE_REJECTED  extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message){
		synchronized(player){
		System.out.println("Fire rejected! ");
		String fireMessege= ((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).getFireMessage(player.getID());
		
		String delims = " ={}[];";
		String[] tokens = StringSpliter.delimStr(fireMessege, delims);
		
		// after update exceeds in observe state
		for(int i=2; i <tokens.length;i=i+2){
			Coordinate coord= Coordinate.makeCoordinate(tokens[i+1].substring(0,4));
			player.update(tokens[i],coord,"C");
		}
		((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).clearShootList();
	 }
		
  }
	
}
