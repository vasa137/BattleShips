package battleships.client;

import javax.swing.JOptionPane;

public class GAME_OVER extends Command {

	@Override
	public void executeMessage(BattleshipsPlayer player, String message) {
		synchronized(player){
		System.out.println("GAME OVER");
		JOptionPane.showMessageDialog(player.frame, "GAME OVER");
		player.frame.getPanel(player.frame.state).setEnabled(false);
        player.setState(Menu.START_STATE);
		}
	}
}
