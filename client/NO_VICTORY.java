package battleships.client;


public class NO_VICTORY extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		 System.out.println("NO_VICTORY ");
		}
		((EndPanel)player.frame.getPanel(MyFrame.END_OF_GAME)).setInfoMessage("NO_VICTORY");
		player.frame.changePanel(MyFrame.END_OF_GAME);
        player.setState(Menu.START_STATE);
	}


}
