package battleships.client;

import battleships.communication.CommunicationCommands;

public class DUPLICATE_NAME extends Command {


	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		String name=null;
		synchronized(player){
			// if name already exists ask for another one
          System.out.println("Name already exists, try with new one! ");
		  System.out.print("Enter name:");
	 	  name=Citaj.String();
		}
		player.setName(name);
		player.send(CommunicationCommands.JOIN_MESSAGE + " " + player.getName());
		
	}


}
