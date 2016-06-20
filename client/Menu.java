package battleships.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Menu {

	private HashMap<Integer, HashMap<String,Command>> stateMap=null;
	
	private int oldstate=-1;
	public static final int OBSERVE_STATE=0;
	public static final int DEPLOY_SHIPS_STATE=1;
	public static final int FIRE_STATE=2;

	public Menu(){
	
		HashMap<String,Command> observeState = new HashMap<String, Command>();
		HashMap<String,Command> deployState= new HashMap<String, Command>();
		HashMap<String,Command> fireState= new HashMap<String, Command>();
	
		observeState.put("state",new STATE());
		observeState.put("quit", new QUIT());
		
		deployState.put("state",new STATE());
		deployState.put("quit", new QUIT());
		deployState.put("deploy", new DEPLOYMENT());
		
		fireState.put("state",new STATE());
		fireState.put("quit", new QUIT());
		fireState.put("fire", new FIRE());
		
		stateMap=new HashMap<Integer,HashMap<String,Command>>();
		stateMap.put(OBSERVE_STATE,observeState);
		stateMap.put(DEPLOY_SHIPS_STATE,deployState);
		stateMap.put(FIRE_STATE,fireState);
	
	}
	
	public String print(BattleshipsPlayer player, int state){
		if (oldstate!=state){
				oldstate=state;
				synchronized(this){
					System.out.println("MENU");
					Set<String> keys = stateMap.get(state).keySet(); 
					int counter=1;
					for(String key: keys){ System.out.println(counter++ + ")"+ key); } 
				}
		}
		else oldstate = state;
		try {
			if(System.in.available() > 0) return Citaj.Line();
		} catch (IOException e) {}
		return null;
    }
	
	public Command getCommand(String key){
		return stateMap.get(oldstate).get(key);
    }

	public Command getCommand(BattleshipsPlayer battleshipsPlayer, String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
