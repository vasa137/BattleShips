package battleships.client;


public class LAYOUT_ACCEPTED extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("LAYOUT ACCEPTED!");
		}
	}
}