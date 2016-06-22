package battleships.client;


public class FIRE_ACCEPTED extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		System.out.println("Fire accepted! ");
		((firePanel)player.frame.getPanel(MyFrame.FIRE_STATE)).clearShootList();
	}

}
