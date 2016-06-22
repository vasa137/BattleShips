package battleships.client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import battleships.common.Bad_Coordinate;
import battleships.common.Coordinate;
import battleships.common.Ship;
import battleships.common.StringSpliter;
import battleships.communication.CommunicationCommands;
import battleships.server.Player;

public class DeployPanel extends JPanel {
	
    private static class PanelForShip extends JPanel{
    	JLabel numofShips;
    	JLabel numofSegment;
    	ButtonGroup orientation;
        JRadioButton horisontal;
        JRadioButton vertical;
        JButton put;
        Ship ship=null;
        BattleshipsPlayer player;
        
    	PanelForShip(final int numberOfShips, final int numberOfSegments,
    		final int maxNumOFSegments,final BattleshipsPlayer player,final StringBuilder s){
    		this.player=player;
    		orientation=new ButtonGroup();
    		horisontal=new JRadioButton("H");
    		horisontal.setSelected(true);
    		orientation.add(horisontal);
    		vertical=new JRadioButton("V");
    		orientation.add(vertical);
    		setLayout(new BorderLayout());
    		
    		JPanel abovePanel=new JPanel();
    		abovePanel.add(new JLabel("Ships:"));
    		abovePanel.add(numofShips=new JLabel(""+numberOfShips));
    		abovePanel.add(new JLabel("Segment:"));
    		abovePanel.add(numofSegment=new JLabel(""+numberOfSegments));
    		abovePanel.add(horisontal);
    		abovePanel.add(vertical);
    		add(abovePanel,BorderLayout.NORTH);
    		
    		
    		JPanel ship=new JPanel();
    		int i=0;
    		for(i=0;i<numberOfSegments;i++) {
    			JButton button=new JButton();
    			button.setPreferredSize(new Dimension(20,20));
    			button.setEnabled(false);
    			button.setBackground(Color.MAGENTA);
    			button.setForeground(Color.GREEN);
    			ship.add(button);
    		}
    		add(ship,BorderLayout.CENTER);
    		
    		put=new JButton("put");
    		put.setPreferredSize(new Dimension(40,20));
    		put.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				int orientation=0;
    				if (horisontal.isSelected()) orientation=Ship.HORIZONTAL;
    				else orientation=Ship.VERTICAL;
    				
    				int number=Integer.parseInt(numofSegment.getText());
    				System.out.println(""+number);
    				Ship ship=new Ship(Integer.parseInt(numofSegment.getText()),orientation);
    				Coordinate coord=player.getTable().getLastPressed();
    				if (coord!=null && !player.getDeployFlag()){
    					try {
    						if (player.getTable().putShip(ship,coord)){
    							s.append(ship+";");
    							int num= Integer.parseInt(numofShips.getText());
    							num--;
    							if (num==0) put.setEnabled(false);
    							numofShips.setText(""+num);
    						}
    						player.getTable().resetLastPressed();
    					} catch (Bad_Coordinate e1) {}
    				}
    			}
    	      });
    		
    		add(put,BorderLayout.SOUTH);
    	}
    }

	private JLabel timeLabel;
    
	private MyFrame myFrame;

	private JLabel TableLableTime;
	
	public void setTimeLabel(String s){
		TableLableTime.setText("Time: " +s);
		if (s.equals("0")) ((InfoPanel)myFrame.getPanel(MyFrame.INFO_STATE)).setInfoMessage("WAIT FOR TIME TO FIRE ");
		else ((InfoPanel)myFrame.getPanel(MyFrame.INFO_STATE)).setInfoMessage("TIME LEFT FOR DEPLOYMENT: " +s );
		
	}
    
	public DeployPanel(MyFrame frame){
	    super(new BorderLayout());
		myFrame=frame;
		TableLableTime=new JLabel();
	}
	
	public void putComponents(){
		myFrame.setResizable(true);
		myFrame.setBounds(300,100,500,600);
		myFrame.setResizable(false);
		String delims = "D()=;S";
		String[] tokens = StringSpliter.delimStr(myFrame.player.getDeployContent(),delims);
		// split message with delimiters
		int sizeTable=Integer.parseInt(tokens[0]);
		// make string
		final StringBuilder newMessage=new StringBuilder("SHIP_LAYOUT "+ myFrame.player.getID() + " ");
		myFrame.player.makeTable(sizeTable);
		
		JPanel offerPanel=new JPanel(new GridLayout(1,tokens.length/2));
		int max=0;
		
		for(int i=0;i<tokens.length/2;i++){
			int numberOfSegment=Integer.parseInt(tokens[2*i+1]);
			if (numberOfSegment>=max) max=numberOfSegment;
			}
		
		System.out.println("MAX :" + max);
		for(int i=0;i<tokens.length/2;i++){
			// printing all options for ship appearance
			int numberOfSegment=Integer.parseInt(tokens[2*i+1]);
			int numberOfShips=Integer.parseInt(tokens[2*i+2]);
			offerPanel.add(new PanelForShip(numberOfShips,numberOfSegment,max,myFrame.player,newMessage));
		}
		add(offerPanel,BorderLayout.NORTH);
		
		add(myFrame.player.getTable().getPanel(),BorderLayout.CENTER);
		
		
		JPanel downPanel=new JPanel();
		final BattleshipsPlayer pl=myFrame.player;
		
		if (myFrame.player.getTimeLeft()>=0) timeLabel=new JLabel("" + myFrame.player.getTimeLeft());
		else timeLabel=new JLabel("Waiting to start");
		
		JButton finish=new JButton("FINISH SHIP DEPLOYMENT");
		finish.setPreferredSize(new Dimension(250,20));
		downPanel.add(finish,BorderLayout.WEST);
		downPanel.add(TableLableTime,BorderLayout.CENTER);
		add(downPanel,BorderLayout.SOUTH);
	
			finish.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e){
			    	if (!newMessage.equals("")){
			    		System.out.println(newMessage.toString());
						 pl.send(newMessage.toString());
			    		((InfoPanel)myFrame.getPanel(MyFrame.INFO_STATE)).setInfoMessage(" SHIP LAYOUT SENT !");
						 myFrame.changePanel(MyFrame.INFO_STATE);
						 pl.send(CommunicationCommands.STATE_REQUEST);
			    	}
			    }
		});
   }
}
	