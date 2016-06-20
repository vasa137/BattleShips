package battleships.client;

public class ACCESS_DENIED extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("Access denied! ");
		}
		// set access flag to finish player while loop in BattleshipsPlayer
		player.setAccessFlag(false);
	}

}
