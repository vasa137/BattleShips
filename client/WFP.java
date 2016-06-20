package battleships.client;

public class WFP extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String x = message.substring(message.indexOf(" ") + 1);
		// just announce state of number of applied players
		synchronized(player){
			System.out.println("Waiting for Players " + x);
			}
		}

	}

