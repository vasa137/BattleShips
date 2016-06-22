package battleships.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;

public class Menu{

	private HashMap<Integer, HashMap<String,Command>> stateMap=null;
	
	private int oldstate=-1;
	
	public static final int OBSERVE_STATE=0;
	public static final int DEPLOY_SHIPS_STATE=1;
	public static final int FIRE_STATE=2;
	public static final int START_STATE=3;
	
	public Menu(){
	
		HashMap<String,Command> startState = new HashMap<String, Command>();
		HashMap<String,Command> observeState = new HashMap<String, Command>();
		HashMap<String,Command> deployState= new HashMap<String, Command>();
		HashMap<String,Command> fireState= new HashMap<String, Command>();
	
		startState.put("join",new JOIN());
		startState.put("exit",new EXIT());
		
		observeState.put("state",new STATE());
		observeState.put("quit", new QUIT());
		
		deployState.put("state",new STATE());
		deployState.put("quit", new QUIT());
		
		
		fireState.put("state",new STATE());
		fireState.put("quit", new QUIT());
		
		stateMap=new HashMap<Integer,HashMap<String,Command>>();
		stateMap.put(START_STATE,startState);
		stateMap.put(OBSERVE_STATE,observeState);
		
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

}
