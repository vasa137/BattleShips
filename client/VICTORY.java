package battleships.client;

public class VICTORY extends Command {


	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		
		synchronized(player){
		System.out.println("VICTORY !");
		}
		// set Access flag to exit game
		player.setAccessFlag(false);
	}

	
}
