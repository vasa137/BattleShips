package battleships.client;


public class NO_VICTORY extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		 System.out.println("NO_VICTORY ");
		}
      
        player.setState(Menu.START_STATE);
	}


}
