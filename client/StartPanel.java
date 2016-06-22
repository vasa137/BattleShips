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
import javax.swing.border.Border;

public class StartPanel extends JPanel {
	
	private Menu menu;
	private JButton join;
	private JButton exit;
    private MyFrame frame;
	
	public StartPanel(final MyFrame frame){
	this.frame=frame;
    setLayout(new BorderLayout());
	JLabel label=new JLabel("GAME MENU:");
	add(label,BorderLayout.NORTH);
	
	label.setPreferredSize(new Dimension(200,50));
	label.setHorizontalAlignment(JLabel.CENTER);
	
	JPanel centerPanel=new JPanel();
	BoxLayout layout=new BoxLayout(centerPanel,BoxLayout.Y_AXIS);
	
	centerPanel.setLayout(layout);
	
	join=new JButton("JOIN");
	centerPanel.add(join);
	join.setAlignmentX(CENTER_ALIGNMENT);
	join.setPreferredSize(new Dimension(70,20));
	
	JLabel labela=new JLabel(" ");
	labela.setFont(new Font(labela.getName(), Font.PLAIN, 5));
	labela.setAlignmentX(CENTER_ALIGNMENT);
	labela.setPreferredSize(new Dimension(70,5));
	centerPanel.add(labela);
	
	exit=new JButton("EXIT");
	exit.setPreferredSize(new Dimension(70,20));
	
	exit.setAlignmentX(CENTER_ALIGNMENT);
	
	centerPanel.add(exit);
	
	add(centerPanel);
    join.addActionListener(new ActionListener(){
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
	
}
