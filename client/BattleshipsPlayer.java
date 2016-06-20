/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import battleships.common.Bad_Coordinate;
import battleships.common.Coordinate;
import battleships.common.NegativDimension;
import battleships.common.Table;
import battleships.communication.Client;
import battleships.communication.CommunicationCommands;

public class BattleshipsPlayer 
{
    private Client client;
    static private HashMap<String,Command> commandMap;
    private  Receiver clientReciever;
    private String clientName;
    private int clientID;
    private boolean accessFlag =true;
    private int state = Menu.OBSERVE_STATE;
    private long endTime;
    private volatile boolean inGame = false;
    private Table myTable = null;
    private HashMap<String,Table> opponentTables;
	private String deployContent;
	
    private class Receiver extends Thread{
    	
    	public void run(){
			// thread that recieve messages, we use this to don't allow thread to block
    		while(!interrupted()){
    		String message="";
    		try {
			 message=client.receive();
			} catch (IOException e) {}
       	     String key=message.split(" ")[0];
       	     Command recieveCommand=commandMap.get(key);
       	     if (recieveCommand==null) continue;
       	     recieveCommand.executeMessage(BattleshipsPlayer.this,message);
    		}
    	}
    }
    
    public BattleshipsPlayer(InetAddress address) throws SocketException, IOException{
    	System.out.print("Enter your name: ");
    	String name=Citaj.String();
        client = new Client(address,name);
        clientReciever = new Receiver();
		//start Reciever thread
        clientReciever.start();
        clientName = name;
        client.send(CommunicationCommands.JOIN_MESSAGE + " " + name);
        Menu menu = new Menu();
        System.out.println("Waiting for server response! \n");
        while(!inGame && accessFlag);
        while(accessFlag){
			// after entering the game, the user will be able to see menu in different states
             String message = menu.print(this,state);
             if(message==null) continue;
             Command playerCommand=menu.getCommand(message.split(" ")[0]);
             if (playerCommand!=null) playerCommand.executeMessage(this,message); 
        }
        clientReciever.interrupt();
    }
    
    
    public static void main(String [] args)
    {
        try 
        {
			//System.out.println("Enter server IP address: ");
            BattleshipsPlayer newPlayer = new BattleshipsPlayer(InetAddress.getByName("localhost"));
        }
        catch (SocketException ex) 
        {
            Logger.getLogger(BattleshipsPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(BattleshipsPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(BattleshipsPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static{
		// initializing map of commands
    	commandMap=new HashMap<String,Command>();
    	commandMap.put(CommunicationCommands.WELCOME_MESSAGE,new WELCOME());
    	commandMap.put(CommunicationCommands.ACCESS_DENIED,new ACCESS_DENIED());
    	commandMap.put(CommunicationCommands.DEPLOY_SHIPS,new DEPLOY_SHIPS());
        commandMap.put(CommunicationCommands.DUPLICATE_NAME,new DUPLICATE_NAME());
        commandMap.put(CommunicationCommands.FIRE_ACCEPTED,new FIRE_ACCEPTED());
        commandMap.put(CommunicationCommands.FIRE_REJECTED,new FIRE_REJECTED());
        commandMap.put(CommunicationCommands.FORCE_DISCONECT,new FORCE_DISCONNECT()); 
        commandMap.put(CommunicationCommands.GAME_OVER,new GAME_OVER());
        commandMap.put(CommunicationCommands.GAME_WON,new GAME_WON());
        commandMap.put(CommunicationCommands.LAYOUT_ACCEPTED, new LAYOUT_ACCEPTED());
        commandMap.put(CommunicationCommands.LAYOUT_REJECTED,new LAYOUT_REJECTED());
        commandMap.put(CommunicationCommands.UPDATE,new UPDATE());
        commandMap.put(CommunicationCommands.NO_VICTORY,new NO_VICTORY());
        commandMap.put(CommunicationCommands.VICTORY,new VICTORY());
        commandMap.put(CommunicationCommands.PASSWORD_REQUIRED,new PASSWORD_REQUIRED());
        commandMap.put(CommunicationCommands.ROUND,new ROUND()); 
        commandMap.put(CommunicationCommands.PASSWORD_REQUIRED,new PASSWORD_REQUIRED());
        commandMap.put(CommunicationCommands.WFP,new WFP()); 
        commandMap.put(CommunicationCommands.R,new R());
        commandMap.put(CommunicationCommands.DS,new DS()); 
        commandMap.put(CommunicationCommands.RANDOM_LAYOUT, new RANDOM_LAYOUT());
    }
    
    public void send(String message) {
		try {
			client.send(message);
		} catch (IOException e) {}
	}
	
    public String receive() {
		// receiving messages through client
    	String message="";
		try {
			message=client.receive();
		} catch (IOException e) {}
		return message;
	}
    
    public Table getTable(){
    	return myTable;
    }
	
	public String getName(){
    	return clientName;
    }
    
    public void setName(String name){
    	clientName=name;
    }
    
    public int getID(){
    	return clientID;
    }
    public void setID(int ID){
    	clientID=ID;
    }

	public synchronized void setAccessFlag(boolean b) {
		accessFlag=b;
		
	}
	public synchronized boolean getAccessFlag() {
		return accessFlag;
	}

	public synchronized void setState(int newState) {
		state=newState;
	}
	public synchronized int  getState() {
		return state;
	}

	public synchronized boolean InGame() {
		return inGame;
	}
	public synchronized void putInGame(){
	    inGame=true;
	}

	public void makeOpponentTables(String tokens[]) {
		// make just logical tables, at the beggining contains only covered fields
		opponentTables = new HashMap<String,Table>();
		for(int i=3;i<tokens.length;i++){
			try {
				if (!tokens[i].equals(clientName)) opponentTables.put(tokens[i],new Table(myTable.numRow(),myTable.numCol(),Table.PUBLIC));
			} catch (NegativDimension e) {}
		}
	}

    public void removeOpponentfromTable(String tokens[]) {
		// if some of players aren't in update list , server will use this command to delete this pklayer from list
       HashMap<String,Table> opponentT=new HashMap<String,Table>();
		for(int i=3;i<tokens.length;i++){
			if (!tokens[i].equals(clientName)) opponentT.put(tokens[i],opponentTables.get(tokens[i]));
		}
		opponentTables=opponentT;
	}

	public synchronized void writeOpponents(){
		StringBuilder string=new StringBuilder("");
	   // printing all opponent tables
		Set<String> keys = opponentTables.keySet();
		int counter=1;
        for(String key: keys){
		string.append(counter++ + ")" + key + "\n");
        }
        System.out.println("Opponents: \n " + string.toString() );
	}

	public synchronized void printOpponentTables() {
		Set<String> keys = opponentTables.keySet();
        for(String key: keys){
        	System.out.println("Opponent "+ key+":\n" );
            System.out.println(opponentTables.get(key));
            System.out.println("\n" );
        }
	}


	public long getTimeLeft() {
		return endTime-System.currentTimeMillis();
	}

	public synchronized void update(String name, Coordinate coord, String substring) {
		if (name==clientName) {
			if (substring=="H")
				try {
					// this method we use in PRIVATE mode
					myTable.hitTable(coord);
				} catch (Bad_Coordinate e) {}
			return;
		}
		else{
			Table table=opponentTables.get(name);
			if(table!=null){
				// this method we use in PUBLIC mode
			if (substring=="H") table.publicSetState(coord,Table.HIT_SHIP);
			else table.publicSetState(coord,Table.WATER);
		   }
		}
	}

	public synchronized void setEndTime(long l) {
		endTime=l;
		
	}

	public void setDeployContent(String receive) {
		deployContent=receive;
		
	}
	public String getDeployContent() {
		return deployContent;
	}
	
	public void makeTable(int size) {
		try {
			myTable=new Table(size,size);
		} catch (NegativDimension e) {}
	}
}
