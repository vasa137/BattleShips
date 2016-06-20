/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.server;

import battleships.common.NegativDimension;
import battleships.common.Table;
import battleships.communication.ClientCommand;
import battleships.communication.PlayerProxy;

import java.io.IOException;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Aktivna klasa koja predstavlja igraÄ�a u igri (na serverskoj strani). SadrÅ¾i ime igraÄ�a i tablu za igru.
Prilikom stvaranja se zadaje igra (videti u nastavku) u kojoj igraÄ� uÄ�estvuje. SluÅ¾i za komunikaciju
sa klijentom predstavljenim klasom PlayerProxy (videti u nastavku). Svoju aktivnost ostvaruje
tako Å¡to Ä�eka da stigne poruka od klijenta kojom se oznaÄ�ava Å¾eljena aktivnost, u skladu sa
prethodno opisanim protokolom. Po prispeÄ‡u poruke pokuÅ¡a njeno izvrÅ¡enje i klijenta obavesti
ishodu upuÄ‡ivanjem odgovarajuÄ‡e poruke.
 * @author POOP
 */
public class Player implements Runnable
{
    private final Thread playerThread = new Thread(this);
    private final String name;
    private Table table;
    private boolean layoutAccepted = false; // SHIP LAYOUT ACCEPTED flag
    final PlayerProxy playerProxy;
    final int id;
    int index;
    
    public Player(PlayerProxy _playerProxy, String _name, int id){
        playerProxy = _playerProxy;
        name = _name;
        this.id = id;
        playerThread.start();
    }
    
    public void run()
    {
    	String msg = "", second_part = "", command = "" ;
    	ClientCommand serverToPlayerCommand = null;
        try{
            while( ! Thread.interrupted() ) {
            	
                String message = playerProxy.receive();
                msg = message.substring(0,message.indexOf(' ')); 
            	second_part = message.substring(message.indexOf(' ')+1); 
            	command = second_part.substring(message.indexOf(' ')+1);
            	try {
					serverToPlayerCommand = Game.instance().getState().getCommandMap().get(msg);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                if(serverToPlayerCommand != null)
					try {
						serverToPlayerCommand.executeServerToPlayerMessage(Game.instance().gameServer,this,command);
					} catch (IOException e) {
						e.printStackTrace();
					}
                System.out.println(msg);
                
            }
        } catch(InterruptedException e) { }
    }

    public void reportMessage(String message)
    {
        try 
        {
            playerProxy.send(message);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Table getTable(){
    	return table;
    }
    public String getName(){
    	return name;
    }
    public void makeTable(int tableSize){
        try {
			table = new Table(tableSize , tableSize);
		} catch (NegativDimension e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public boolean layoutAcc(){
    	return layoutAccepted;
    }
	
	
    public void setLayoutAcc(){
    	layoutAccepted = true;
    }

	public int getID() {
		return id;
	}
	
	public void allocateTable(){
		try {
			table = new Table(Game.instance().getTableSize(),Game.instance().getTableSize());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (NegativDimension e) {
			e.printStackTrace();
		}
	}
}
