package battleships.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import battleships.common.Coordinate;

public class firePanel extends JPanel implements ActionListener{
	
	// fireShoot contains opponent and coord
	 static class FireShoot{
		JButton button;
		Color color;
		public FireShoot(JButton b, Color c){
			button=b;
			color=c;
		}
		
		public String toString(){
			String [] tokens=button.getActionCommand().split(" ");
		    return "{"+tokens[1]+"}" + Coordinate.makeCoordinate(tokens[0]) +";";
		}
	}
	
	// save fireShootlist , it is necessary to remember this because we want to gave user option to change this list
	private LinkedList<FireShoot> fireShootlist;
	
	int shootNum=0;

	private MyFrame frame;

	private JLabel fireText;

	private JLabel roundNum;

	private JLabel fireTime;
	
	private int number;

	private JLabel variableOperative;
	
	public String getFireMessage(int id){
		//making acceptable format of fire message
		StringBuilder newMessage=new StringBuilder("FIRE "+ id +" [");
		for (FireShoot temp :fireShootlist) {
			newMessage.append(temp);
		}
		newMessage.append("]");
		return newMessage.toString();
	}
	
	public firePanel(MyFrame frame){
		this.frame=frame;
		fireShootlist=new LinkedList<FireShoot>();
		fireText=new JLabel("");
		roundNum=new JLabel("");
	}
	
	
	public void setOpperativeSegments(){
		System.out.println("Usao sam da postavim operativne segmente");
		variableOperative.setText(""+ frame.player.getTable().numberOfOperativeSegments());
	}
	
	public void clearShootList(){
		 if (fireShootlist!=null) {
			 fireShootlist.clear();
		     fireText.setText(getFireMessage(frame.player.getID()));
		    
		 }
	}
		public void putComponents() {
			setSize(500,500);
			setLayout(new BorderLayout());
		    final BattleshipsPlayer player=frame.player;
		    fireShootlist.clear();
		    JPanel upPanel=new JPanel(new GridLayout(2,1));
		    roundNum.setText("FIRE TIME");
		    upPanel.add(roundNum);
		    upPanel.add(fireText);
		    
		    add(upPanel,BorderLayout.NORTH);
		    
			fireShootlist=new LinkedList<FireShoot>();
			int tableNumber = player.opponentTables.size();
			JPanel tablePanel=new JPanel();
			for(String s: player.opponentTables.keySet()) {
				tablePanel.add(player.opponentTables.get(s).getPanel());
				if (number++==0) player.opponentTables.get(s).addFireListener(this);
			}
		    
			add(tablePanel,BorderLayout.CENTER);
			
			JPanel myTablePanel=new JPanel(new BorderLayout());
			myTablePanel.add(new JLabel("MY TABLE:"),BorderLayout.NORTH);
			myTablePanel.add(player.getTable().getPanel(),BorderLayout.CENTER);
			player.getTable().getPanel().setEnabled(false);
			JPanel infoPanel=new JPanel();
			JLabel operativeSegments=new JLabel("Operative segments:");
			infoPanel.add(operativeSegments);
			variableOperative=new JLabel(""+player.getTable().numberOfOperativeSegments());
			JLabel constOperative=new JLabel("/ "+player.getTable().numberOfOperativeSegments());
			infoPanel.add(variableOperative);
			infoPanel.add(constOperative);
			myTablePanel.add(infoPanel,BorderLayout.SOUTH);
			
			add(myTablePanel,BorderLayout.EAST);
			
			JPanel downPanel=new JPanel();
		    JButton fire =new JButton("Fire");
		    fire.setPreferredSize(new Dimension(50,20));
		    downPanel.add(fire);
		    JLabel fireTimetext=new JLabel("Fire time: ");
			downPanel.add(fireTimetext);
			fireTime=new JLabel("Wait to Start");
			downPanel.add(fireTime);
			add(downPanel,BorderLayout.SOUTH);
			
			fire.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					player.send(getFireMessage(player.getID()));
				}
			});
			
    }

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button=(JButton)e.getSource();
			if (button.getBackground() != Color.WHITE){
				fireShootlist.add(new FireShoot(button,button.getBackground()));
				button.setBackground(Color.WHITE);
			}
			else {
				  for (int i = 0; i < fireShootlist.size(); i++) {
			           FireShoot f= fireShootlist.get(i);
			           if (f.button.equals(button)) {
			        	   button.setBackground(f.color);
			        	   fireShootlist.remove(i);
			        	   break;
			             }
			          }
			           
			  }
			fireText.setText(getFireMessage(frame.player.getID()));
		}
		
		public void setTimeLabel(String s) {
			if (fireTime!=null) fireTime.setText(s);
		}
		public void setRoundNum(String string) {
			roundNum.setText("ROUND "+ string+" BEGINS");
		}
		
		
}
