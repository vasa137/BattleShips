package battleships.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import battleships.communication.ClientCommand;

//every gameState has own hashmap of allowed commands
public abstract class GameState {
	public abstract void execute(BattleOverseer battleOverseer) throws InterruptedException, IOException;
	public abstract GameState setNextState(); 
	public abstract String stateDescription() throws SocketException;
	static protected Timer timer = null;
	protected HashMap<String, ClientCommand> commandMap = new HashMap<>();
	public HashMap<String, ClientCommand> getCommandMap(){	return commandMap;	}
}
