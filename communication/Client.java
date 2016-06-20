/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author POOP
 */
public class Client extends SocketCommunicator
{
    private final InetAddress serverAddress;
    
    
    
    public Client(InetAddress _serverAddress, String name) throws SocketException
    {
        serverAddress = _serverAddress;
        
    }
    
   

	public void send(String message) throws IOException
    {
        send(message, serverAddress, SERVER_PORT);
    }
    
}
