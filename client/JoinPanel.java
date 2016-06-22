package battleships.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JoinPanel extends JPanel {

	private JTextField[] textField;
	 private MyFrame frame;
	 
	public JoinPanel(final MyFrame frame){
		this.frame=frame;
	String[] labels = {
			"Server IP address: ",
	        "Name: ",
			"Password: ", 
			"Image: "
			};
	final int numPairs = labels.length;
	setLayout(new BorderLayout());
	add(new JLabel("JOIN THE GAME:", JLabel.CENTER),BorderLayout.PAGE_START);
	textField=new JTextField[labels.length];

	JPanel leftPanel=new JPanel();
	BoxLayout leftlayout=new BoxLayout(leftPanel,BoxLayout.Y_AXIS);
	leftPanel.setLayout(leftlayout);
	
	JPanel rightPanel=new JPanel();
	BoxLayout rightlayout=new BoxLayout(rightPanel,BoxLayout.Y_AXIS);
	
	rightPanel.setLayout(rightlayout);
	
	
	for (int i = 0; i < numPairs; i++) {
		JPanel p=new JPanel();
	    JLabel label = new JLabel(labels[i]);
	    label.setPreferredSize(new Dimension(110,50));
	    p.add(label);
	    textField[i] = new JTextField(10);
	    textField[i].setPreferredSize(new Dimension(50,25));
	    p.add(textField[i]);
	    leftPanel.add(p);
	}
	
	add(leftPanel,BorderLayout.CENTER);
	JPanel panel=new JPanel();
	 JButton finish=new JButton("finish");
	 panel.add(finish);
	 add(panel,BorderLayout.PAGE_END);
	 finish.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
	    		String message="";
	    		for (int i = 0; i <numPairs; i++) {
	    		    message+=textField[i].getText()+" ";
	    		}
	    		if (message.split(" ").length>=3) new JOIN().executeMessage(frame.player, message);
	    	} 
	 });
 }
	
}