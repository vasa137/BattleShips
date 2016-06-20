/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.server;

import battleships.client.Citaj;
import battleships.common.Table;
import battleships.communication.PlayerProxy;
import battleships.communication.Server;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Citanjem sa standardnog ulaza moÅ¾e da se zada pristupna lozinka, moÅ¾e
da se definiÅ¡e veliÄ�ina table za igru, broj i veliÄ�ina brodova, vreme predviÄ‘eno za rasporeÄ‘ivanje
brodova i vreme predviÄ‘eno za svaku rundu. MoÅ¾e da se zada najveÄ‡i broj igraÄ�a, najmanje 2
igraÄ�a. Bitka moÅ¾e da se pokrene i da se prekine. MoÅ¾e da se traÅ¾i prikaz table svakog igraÄ�a
nakon svake runde. MoÅ¾e da poÅ¡alje poruku svim igraÄ�ima ukljuÄ�enim u igru.
 * @author POOP
 */
 
 
//game goes through 4 passive states, we have reference to the current state, and it s neccessary to have a supervisor of the battle (active class)

public class Game {
	private final int minPlayers = 2;
    private static Game instance;
    private GameState gamestate = WFP.getInstance();
    private String password = "j";
    private int tableSize = 4, numberOfShips = 3,  maxPlayers = 2;
    private long timeForDeploy = 5000, roundTime = 15000; 
    int [] shipCount; // index = number of segments, value = number of ships with index segments
    
    ArrayList<Player> players = new ArrayList<>(), activePlayers;
    Server gameServer;
    private BattleOverseer battleOverseer = null;
     
    private void gameInitParams(){
    	/*
    	System.out.print("Enter game password : ");
    	password = Citaj.String();
    	System.out.print("Enter table size : ");
    	tableSize = Citaj.Int();
    	System.out.print("Enter number of ships : ");
    	numberOfShips = Citaj.Int();
    	*/
    	
    	shipCount = new int[tableSize];
    	for(int i = 0 ; i < 3 ; i++){
    		shipCount[i + 1] = 1;
    	}
    	/*
    	for(int i = 0; i < numberOfShips; i++){
    		System.out.print("Enter number of segments for " + i + 1 + ". ship : ");
    		int x = Citaj.Int();
    		shipCount[x]++;
    	}
    	*/
    
    	
    	/*
    	System.out.print("Enter time for ship deployment : ");
    	timeForDeploy = Citaj.Long();
    	System.out.print("Enter round time : ");
    	roundTime = Citaj.Long();
    	System.out.print("Enter max number of players : ");
    	maxPlayers = Citaj.Int();
    	*/
    }
    
    public void showAllTables(){
    	for(int i = 0; i<players.size(); i++){
    		System.out.println("Player :  "+ players.get(i).getName());
    		System.out.println(players.get(i).getTable());
    	}
    }
    
    public void startGame(){ // regulate game with supervisor active class
    	if(battleOverseer == null && players.size() >= minPlayers){
    		gamestate = gamestate.setNextState();
    		battleOverseer = new BattleOverseer(this); 
    	}
    }
    
	//abandon game
    public void stopGame(){
    	if(battleOverseer != null){
    		battleOverseer.interrupt();
    		battleOverseer = null;
    	}
    	activePlayers = null;
    	players.clear(); 
    	gameServer.reset(); // reset server parameters and remove elements from maps
    }
    
    private Game() throws SocketException {
         gameServer = new Server(this);
         gameInitParams();
    }
    
    public static Game instance() throws SocketException{
        if( instance == null ){
            instance = new Game();
        }
        return instance;
    }
    
    public Player newPlayer(PlayerProxy pp, String name, int id)
    {
        if(players.size() < maxPlayers) {
        	Player p = new Player(pp, name, id);
        	players.add(p);
             synchronized(gamestate){
        		if(players.size() == maxPlayers) startGame();
        	}
        	return p; // returns added player
        }
        return null;       
    }
    
    public void sendMessageToAllPlayers(String message)
    {
        for(Player p : players)
            p.reportMessage(message);
    }
        
    public static void main(String []args)
    {
        try 
        {
            Game.instance();
        }
        catch (SocketException ex) 
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	public GameState getState() {
		return gamestate;
	}
	
	void setState(GameState gameState){
		gamestate = gameState;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public long getDeployTime(){
		return timeForDeploy;
	}
	
	public long getRoundTime(){
		return roundTime;
	}
	
	public int getTableSize(){
		return tableSize;
	}

	public int getMinPlayers(){
		return minPlayers;
	}
	
	public ArrayList<Player> getActivePlayers(){
		return activePlayers;
	}
	
	public ArrayList<Player> clonePlayersToActive(){
		activePlayers = (ArrayList<Player>) players.clone();
		return activePlayers;
	}
	
	public BattleOverseer getBattleOverseer(){
		return battleOverseer;
	}
}
