package battleships.client;


public class GAME_WON extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("GAME WON "+ message.split(" ")[1]);
		((EndPanel)player.frame.getPanel(MyFrame.END_OF_GAME)).setInfoMessage("GAME WON "+ message.split(" ")[1]);
		player.frame.changePanel(MyFrame.END_OF_GAME);
		player.setState(Menu.START_STATE);
		}
	}
}
