package battleships.client;

import battleships.common.StringSpliter;

public class ROUND extends Command {
	
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String delims = " =[];";
		String[] tokens = StringSpliter.delimStr(message, delims);
		
		int roundNum=Integer.parseInt(tokens[1]);
		synchronized(player){
			System.out.println("ROUND "+ roundNum + " BEGINS!" );
		}
		player.setEndTime(Integer.parseInt(tokens[2])+System.currentTimeMillis());
		// if this is first round battleships player will make all tables
		// In tokens are names of opponents
		if (roundNum==1) player.makeOpponentTables(tokens);
		else player.removeOpponentfromTable(tokens);
		//Set fire state to allow fire_state mode of menu commands
		System.out.println( "My table: \n"+player.getTable());
		player.printOpponentTables();
		
		
		player.setState(Menu.FIRE_STATE);
	}
}