package battleships.client;

public class QUIT extends Command{

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		player.send("QUIT "+player.getID());
			// set access flag to finish player while loop in BattleshipsPlayer
		player.setAccessFlag(false);	
	}	
}
