package battleships.client;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Spring;

public class MyFrame extends JFrame{
    Menu menu;
	BattleshipsPlayer player;
	
	public static final int START_STATE=0;
	public static final int JOIN_STATE=1;
	public static final int DEPLOY_SHIPS_STATE=2;
	public static final int FIRE_STATE=3;
	public static final int INFO_STATE = 4;
	static final int END_OF_GAME = 6;

	private HashMap<Integer, JPanel> panelMap=null;
	int state=-1;
	
	public MyFrame(Menu meni,final BattleshipsPlayer player){
		super("BattleShip");
		menu=meni;
		this.player=player;
	
		setBounds(200,200,300,250);
		initPanels();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e){
			 int confirm=JOptionPane.showOptionDialog(MyFrame.this,"Are You Sure to Close this Application?","Exit Confirmation",
					 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,  null, null, null);
             if (confirm == JOptionPane.YES_OPTION){
                 System.exit(1);
             }
         }
		});
     
		this.setLayout(new BorderLayout());
		changePanel(START_STATE);
	}
	
	private void initPanels() {
		panelMap= new HashMap<Integer,JPanel>();
		panelMap.put(START_STATE, new StartPanel(this));
        panelMap.put(JOIN_STATE,new JoinPanel(this));
        panelMap.put(DEPLOY_SHIPS_STATE,new DeployPanel(this));
        panelMap.put(FIRE_STATE, new firePanel(this));
        panelMap.put(INFO_STATE,new InfoPanel(this));
        panelMap.put(END_OF_GAME,new EndPanel(this));
	}

	public void changePanel(int newstate){
		if(newstate!=state){
			setResizable(true);
			
			setSize(300,250);
			setResizable(false);
			state=newstate;
			this.getContentPane().removeAll();		
			JPanel p=panelMap.get(state);
			if (state==DEPLOY_SHIPS_STATE) ((DeployPanel)p).putComponents();
			if(state==FIRE_STATE) ((firePanel)p).putComponents();
			add(p);
			setVisible(true);
		}
	}
	
	public JPanel getPanel(int state){
		return panelMap.get(state);
	}
	private boolean setTimeFlag=false;
	public void setTime(){setTimeFlag=true;}
	
     public boolean isSetTime(){
    	 return setTimeFlag;
     }

	public void setRoundNum(String string) {
		 JPanel p=panelMap.get(state);
		((firePanel)p).setRoundNum(string);
	}

}
