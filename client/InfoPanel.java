package battleships.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {
	
	private JLabel infoMessageLabel;
	
	InfoPanel(MyFrame frame){
		setLayout(new BorderLayout());
		infoMessageLabel=new JLabel(" ");
		infoMessageLabel.setPreferredSize(new Dimension(200,40));
		infoMessageLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(infoMessageLabel, BorderLayout.CENTER);
	}
	
	public void setInfoMessage(String string){
		infoMessageLabel.setText(string);
	}
}
