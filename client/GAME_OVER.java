package battleships.client;


public class GAME_OVER extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("GAME OVER");
       
        player.setState(Menu.START_STATE);
		}
	}
	
}
