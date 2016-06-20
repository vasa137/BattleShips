package battleships.client;

public class DS extends Command {
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String x = message.substring(message.indexOf(" ") + 1);
		// set end time left 
		player.setEndTime(Integer.parseInt(x)+System.currentTimeMillis());
		synchronized(this){
			System.out.println("TIME LEFT FOR DEPLOY : "+ Integer.parseInt(x)/1000 );
		 }
		}
}