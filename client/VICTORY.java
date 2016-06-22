package battleships.client;

public class VICTORY extends Command {


	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		
		synchronized(player){
		System.out.println("VICTORY !");
		}
		((EndPanel)player.frame.getPanel(MyFrame.END_OF_GAME)).setInfoMessage("VICTORY");
		player.frame.changePanel(MyFrame.END_OF_GAME);
		// set Access flag to exit game
		player.setState(Menu.START_STATE);
	}

	
}
