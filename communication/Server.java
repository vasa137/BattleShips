/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.communication;

import battleships.server.Game;
import battleships.server.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author POOP
 */
public class Server extends SocketCommunicator implements Runnable
{
    final Hashtable<Integer, PlayerProxy> connectedPlayers = new Hashtable<>(); //hastable of all playerproxies ->spectators and active players, mapped by id
    final Hashtable<String, Player> nameMap = new Hashtable<>(); // for name_duplciation ad constant complexity
    final Hashtable<Integer, Player> playersMap = new Hashtable<>(); //hastable of all players ->spectators and active players, mapped by id
    private final Thread serverThread = new Thread(this);
    int clientID;
    final Game game;
        
    public Server(Game _game) throws SocketException{
        super(SERVER_PORT);
        game = _game;
        serverThread.start();
    }
    
    public void reset(){
    	connectedPlayers.clear();
    	nameMap.clear();
    	playersMap.clear();
    	clientID = 0; // reset counter ID
    }
    
    @Override
    public void run(){
        String msg = "", second_part = "";
        ClientCommand clientCommand = null;
        while( ! Thread.interrupted() ){
            try{
                String message = receive();
                
                //parse
                if(message.contains(" ")){
                	msg = message.substring(0,message.indexOf(' '));
                	second_part = message.substring(message.indexOf(' ')+1); 
                }
                else{
                	msg = message;
                	second_part = "";
                }

            	
            	//execute command
            	clientCommand = game.getState().getCommandMap().get(msg);
                if(clientCommand != null){
                	clientCommand.executeClientMessage(this,second_part,msg);
                }
             
            }
            catch(IOException e) {}
        }
        System.out.println("... game server ended.");
    }
    

	public Hashtable<String, Player> getNameMap() {
		return nameMap;
	}

}
