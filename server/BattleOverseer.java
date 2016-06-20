package battleships.server;

import java.io.IOException;

//It starts when max players applied for game
public class BattleOverseer extends Thread {
	Game myGame;
	
	public BattleOverseer(Game game){ myGame = game; start(); }
	
	public void run(){
		//starts in SHIP DEPLOY STATE
		while(!interrupted()){
			try {
				myGame.getState().execute(this); // execute current state
			} 
			catch(InterruptedException e) {}
			catch(IOException e){} 
			
			myGame.setState(myGame.getState().setNextState());  // go to next state
		}
	}
}
