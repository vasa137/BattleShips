package battleships.client;

public class FORCE_DISCONNECT extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("FORCE DISCONNECT!");
		}
		player.setState(Menu.START_STATE);
	}
}
