package battleships.client;

import battleships.common.Coordinate;
import battleships.common.StringSpliter;
import battleships.communication.CommunicationCommands;

public class ROUND extends Command {
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String delims = " =[];";
		String[] tokens = StringSpliter.delimStr(message, delims);
		
		int roundNum=Integer.parseInt(tokens[1]);
		synchronized(player){
			System.out.println("ROUND "+ roundNum);
		}
		
		player.setEndTime(Integer.parseInt(tokens[2])+System.currentTimeMillis());
		// if this is first round battleships player will make all tables
		// In tokens are names of opponents
		if (roundNum==1) player.makeOpponentTables(tokens);
		else player.removeOpponentfromTable(tokens);
		//Set fire state to allow fire_state mode of menu commands
		System.out.println( "My table: \n"+player.getTable());
		player.printOpponentTables();
		player.frame.changePanel(MyFrame.FIRE_STATE);
		player.frame.setRoundNum(""+roundNum);
		player.send(CommunicationCommands.STATE_REQUEST);
		
		 
		    String fireMessage=((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).getFireMessage(player.getID());
		    if (fireMessage!=null){
			String newdelims = " ={}[];";
			String[] newtokens = StringSpliter.delimStr(fireMessage, newdelims);
			// after update exceeds in observe state
			for(int i=2; i <newtokens.length;i=i+2){
				Coordinate coord= Coordinate.makeCoordinate(newtokens[i+1].substring(0,4));
				player.update(newtokens[i],coord,"C");
			}
		   }
			
			((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).clearShootList();
		
	}
	
}