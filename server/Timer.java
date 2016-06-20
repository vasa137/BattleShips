package battleships.server;

public class Timer extends Thread {
	public Timer(GameState gamestate, long timeToSleep){
		sleepTime = timeToSleep;
		gameState = gamestate;
		start();
	}

	private long sleepTime, startTime;
	private GameState gameState;
	
	public void run(){
		startTime= System.currentTimeMillis(); // holds a startTime before sleep
		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {}
		
		synchronized(gameState){
			gameState.notify();
		}
	}
	
	public long getTimeLeft(){
		long time = sleepTime - ( System.currentTimeMillis() - startTime );
		if(time >= 0 ) return time;
		else return 0;
	}
}
