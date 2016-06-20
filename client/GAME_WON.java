package battleships.client;


public class GAME_WON extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("GAME WON "+ message.split(" ")[1]);
		
		player.setState(Menu.START_STATE);
		}
	}
}
