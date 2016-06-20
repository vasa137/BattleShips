package battleships.client;

import java.util.LinkedList;
import java.util.List;

import battleships.common.Coordinate;


public class FIRE extends Command {
	
	// fireShoot contains opponent and coord
	private class FireShoot{
		String opponent;
		Coordinate coord;
		public FireShoot(String s,Coordinate c){
			opponent=s;
			coord=c;
		}
		
		public String toString(){
		 return "{"+opponent+"}" + coord +";";
		}
	}
	
	// save fireShootlist , it is necessary to remember this because we want to gave user option to change this list
	LinkedList<FireShoot> fireShootlist;
	int shootNum=0;
	
	public String getFireMessage(int id){
		//making acceptable format of fire message
		StringBuilder newMessage=new StringBuilder("FIRE "+ id +" [");
		for (FireShoot temp :fireShootlist) {
			newMessage.append(temp);
		}
		newMessage.append("]");
		return newMessage.toString();
	}
	
		@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
			fireShootlist=new LinkedList<FireShoot>();
			
			
			
			
			
			synchronized(player){
			 System.out.println(" ENTER COORDINATE AND NAME OF OPPONENT! " );
			 
			 player.printOpponentTables();
			 System.out.println(" Commands: add, remove and finish" );
			}
		     while (player.getState()==Menu.FIRE_STATE){ 
		    	           
				           synchronized(player){
						   System.out.println("fire>");
						 
						    String fireCommand=Citaj.String();
						    
							if (fireCommand.equals("add")) {
								//adding fire element in list
								    int row=Citaj.Int();
									int column=Citaj.Int();
									String opponent=Citaj.String();
								    fireShootlist.add(shootNum++,new FireShoot(opponent, new Coordinate(row,column)));
							}
							else if (fireCommand.equals("remove")) {
								 int Num=Citaj.Int();
								 // remove fire element
								 fireShootlist.remove(Num);
								 shootNum--;
							}
							else if (fireCommand.equals("finish")) {
								// finish fire command
								player.send(getFireMessage(player.getID()));
								fireShootlist.clear();
								shootNum=0;
								break;
							}
							 System.out.println(getFireMessage(player.getID()) + "\n");
				     } 
		     }
	}
}
