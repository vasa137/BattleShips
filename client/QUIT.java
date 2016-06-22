package battleships.client;

public class QUIT extends Command{

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		player.send("QUIT "+player.getID());
		
		player.setState(Menu.START_STATE);
	}	
}
