package battleships.client;

public class R extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		
		String [] tokens=message.split(" ");
		//split tokens to get Round tim left
		int x=Integer.parseInt(tokens[2]);
		//
		player.setEndTime(x+System.currentTimeMillis());
		 synchronized(player){
			 // synchronized player allows us to write without preemtion
			System.out.println("TIME LEFT"+ x/1000 +" IN ROUND "+ tokens[1]);
		}
	}
}