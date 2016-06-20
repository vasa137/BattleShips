package battleships.client;

public class EXIT extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		player.setAccessFlag(false);
	}

}
