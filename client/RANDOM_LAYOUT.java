package battleships.client;

import battleships.common.StringSpliter;
import battleships.communication.CommunicationCommands;

public class RANDOM_LAYOUT extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		StringBuilder newMessage=new StringBuilder(CommunicationCommands.SHIP_LAYOUT + " "+ player.getID() + " ");
		System.out.println("Dep cont : "  + player.getDeployContent());
		if(player.getTable() == null){
			player.makeTable(Integer.parseInt(StringSpliter.delimStr(player.getDeployContent(), "D()=;S")[0]));
		}
		String layoutMessage = player.getTable().randomLayout(player.getDeployContent());
		newMessage.append(layoutMessage);
		player.send(newMessage.toString());
		System.out.println(newMessage);
	}
}
