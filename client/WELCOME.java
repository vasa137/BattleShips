package battleships.client;

public class WELCOME extends Command {


	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String delims = " ";
		String[] tokens = message.split(delims);
		player.setID(Integer.parseInt(tokens[1]));
		System.out.println("WELCOME");
		// set flag that tells us that our player is in game
		player.putInGame();
	}
	
}
