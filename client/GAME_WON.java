package battleships.client;


public class GAME_WON extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.print("GAME WON "+ message.split(" ")[1]);
		player.setAccessFlag(false);
		}
	}
}
