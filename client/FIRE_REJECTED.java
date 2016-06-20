package battleships.client;


public class FIRE_REJECTED  extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message){
		synchronized(player){
		System.out.println("Fire rejected! ");
		}	
	}
	
}
