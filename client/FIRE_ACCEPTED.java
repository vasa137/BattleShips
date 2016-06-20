package battleships.client;


public class FIRE_ACCEPTED extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("Fire accepted! ");
		}
	}

}
