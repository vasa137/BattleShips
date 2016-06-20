package battleships.client;


import java.io.IOException;

import battleships.common.Coordinate;
import battleships.common.Ship;
import battleships.common.StringSpliter;
import battleships.communication.CommunicationCommands;

public class DEPLOYMENT extends Command {
	
	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
	
		String delims = "D()=;S";
		String[] tokens = StringSpliter.delimStr(player.getDeployContent(),delims);
		// split message with delimiters
		int sizeTable=Integer.parseInt(tokens[0]);
		
		// make string
		StringBuilder newMessage=new StringBuilder("SHIP_LAYOUT "+ player.getID() + " ");
		
		player.makeTable(sizeTable);
		
		synchronized(player){
		for(int i=0;i<tokens.length/2;i++){
			// printing all options for ship appearance
			int numberOfSegment=Integer.parseInt(tokens[2*i+1]);
			int numberOfShips=Integer.parseInt(tokens[2*i+2]);
			System.out.println("NUMBER OF SHIPS:" +numberOfShips + ", NUMBER OF SEGMENTS:" + numberOfSegment);
			}
		
		
		System.out.println("Enter first coordinate(Row, Column), segment number and orientation: H(HORIZONTAL) or V(VERTICAL)");
		System.out.println("Commands: put and finish");
		}
		 String commandName=null;
		 int row=0;
		 int column=0;
		 int segmentNum=0;
	     char o=' ';
		while(player.getState()==Menu.DEPLOY_SHIPS_STATE&&player.getTimeLeft()<0){
			// sending message to get periodical message from server 
			player.send(CommunicationCommands.STATE_REQUEST);
			   try {
				     synchronized(player){
						 // printing left time
				        System.out.print("TIME LEFT: " + player.getTimeLeft() + " deploy>");
				    }
				    
				   if (System.in.available()>0) {
				      commandName=Citaj.String();
			        }
				   else continue;
			   }
			   catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//enter command "put " "to add segment 
			    if (commandName=="put"){
			    synchronized(player){
					    try {
							// synonym for KB hit (Keyboard Hit)
							if (System.in.available()>0) {
								row=Citaj.Int();
								column=Citaj.Int();
								segmentNum=Citaj.Int();
								o=Citaj.Char();
							}
							else continue;
						} catch (IOException e) {
							e.printStackTrace();
						}
			    }
				int orientation=Ship.VERTICAL;
				if(o=='H') orientation=Ship.HORIZONTAL;
				// making coordinate
				Ship newShip=new Ship(segmentNum,orientation);
				newShip.setFirstCoordinate(new Coordinate(row,column));
				newMessage.append(newShip);
				newMessage.append(";");
			    }
				// enter command "finish " "to send ship layout
			    else if(commandName=="finish"){
			    player.send(newMessage.toString());
		   }
			
	  }
	}
}
