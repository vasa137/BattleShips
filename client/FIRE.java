package battleships.client;

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
	List<FireShoot> fireShootlist;
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
			
			synchronized(player){
			 System.out.println(" ENTER COORDINATE AND NAME OF OPPONENT! " );
			 
			 player.printOpponentTables();
			 System.out.println(" Commands: add, remove and finish" );
			}
		     while (player.getState()==Menu.FIRE_STATE){ 
				    synchronized(player){
						   System.out.println("TIME LEFT: " + player.getTimeLeft() +" fire>");
					    
						     String fireCommand=Citaj.String();
							if (fireCommand=="add") {
								//adding fire element in list
								    int row=Citaj.Int();
									int column=Citaj.Int();
									String opponent=Citaj.String();
								    fireShootlist.add(shootNum++,new FireShoot(opponent, new Coordinate(row,column)));
							}
							else if (fireCommand=="remove") {
								 int Num=Citaj.Int();
								 // remove fire element
								 fireShootlist.remove(Num);
								 shootNum--;
							}
							else if (fireCommand=="finish") {
								// finish fire command
								player.send(getFireMessage(player.getID()));
								fireShootlist.clear();
								shootNum=0;
								break;
							}
				     } 
		     }
	}
}
