package battleships.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EndPanel extends JPanel {
	
	private Menu menu;
	private JButton newgame;
	private JButton exit;
    private MyFrame frame;
	private JLabel gamestate;
	
	public EndPanel(final MyFrame frame){
	this.frame=frame;
	
	    setLayout(new BorderLayout());
		JLabel label=new JLabel("GAME REPORT:");
		add(label,BorderLayout.NORTH);
		
		label.setPreferredSize(new Dimension(200,50));
		label.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel centerPanel=new JPanel();
		BoxLayout layout=new BoxLayout(centerPanel,BoxLayout.Y_AXIS);
		
		centerPanel.setLayout(layout);
	
	
		newgame=new JButton("NEW GAME");
		centerPanel.add(newgame);
		newgame.setAlignmentX(CENTER_ALIGNMENT);
		newgame.setPreferredSize(new Dimension(70,20));
		
		JLabel labela=new JLabel(" ");
		labela.setFont(new Font(labela.getName(), Font.PLAIN, 5));
		labela.setAlignmentX(CENTER_ALIGNMENT);
		labela.setPreferredSize(new Dimension(70,5));
		centerPanel.add(labela);
		
		exit=new JButton("EXIT");
		exit.setPreferredSize(new Dimension(70,20));
		exit.setAlignmentX(CENTER_ALIGNMENT);
		centerPanel.add(exit);
		
		gamestate=new JLabel(" ");
	    add(gamestate);
	    gamestate.setPreferredSize(new Dimension(70,20));
		add(centerPanel);
	
    newgame.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
    	  frame.changePanel(MyFrame.JOIN_STATE);
    	} 
      }
    );
    
    exit.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
    	  frame.player.interrupt();
    	  frame.dispose();
    	} 
      }
    );
  }
	

	public void setInfoMessage(String string) {
		   gamestate.setText(string);
	}
}
