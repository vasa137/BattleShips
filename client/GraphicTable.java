package battleships.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import battleships.client.firePanel.FireShoot;
import battleships.common.Bad_Coordinate;
import battleships.common.Coordinate;
import battleships.common.NegativDimension;
import battleships.common.Ship;
import battleships.common.StringSpliter;
import battleships.common.Table;

public class GraphicTable extends Table implements ActionListener{

	JPanel panel;
	JButton[][] table;
	Coordinate lastPressed=null;
    private HashMap<Integer, Color> colorMap=null;
	private MyFrame frame;
    
	public GraphicTable(int rowNumber, int columnNumber, String name, MyFrame frame) throws NegativDimension {
		this(rowNumber, columnNumber,PRIVATE, name, frame);
	}

	public GraphicTable(int rowNumber, int columnNumber,int status, String name, MyFrame frame)  throws NegativDimension {
		super(rowNumber, columnNumber,status);
		initColors();
		this.frame=frame;
		JPanel namePanel=new JPanel(new BorderLayout());
		namePanel.add(new JLabel("TABLE OF PLAYER "+ name,JLabel.CENTER),BorderLayout.CENTER);
		/*BufferedImage myPicture = ImageIO.read(new File(image));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		namePanel.add(picLabel,BorderLayout.EAST);
		*/
		JPanel matrixPanel=new JPanel(new GridLayout(rowNumber, columnNumber));
		table=new JButton[rowNumber][columnNumber];
		for(int i=0;i<numRow();i++){
			for(int j=0;j<numCol();j++){
				table[i][j]=new JButton();
				table[i][j].setActionCommand((new Coordinate(i,j)).toString() + " " + name);
				table[i][j].addActionListener(this);
				matrixPanel.add(table[i][j]);
			}
		}
		panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setSize(200, 200);
		panel.add(namePanel,BorderLayout.NORTH);
		panel.add(matrixPanel,BorderLayout.CENTER);
		repaint();
	}

	

	private void initColors() {
		 colorMap=new HashMap<Integer, Color>();
		 colorMap.put(COVERED,Color.black);
		 colorMap.put(WATER,Color.blue);
		 colorMap.put(HIT_SHIP,Color.red);
		 colorMap.put(OPERATIVE_SHIP,Color.green);
	}
	
	public JPanel getPanel() {
		return panel;
	}
	private void repaint(){
		for(int i=0;i<numRow();i++){
			  for(int j=0;j<numCol();j++){
				  // add chars depends on mode 
				  if (visible==PRIVATE) privatePaint(i,j);
				  else publicPaint(i,j);
			  }
		  }
	}

	private void publicPaint(int i, int j) {
		Color c=colorMap.get(matrixStatus[i][j]);
		if (c!=colorMap.get(COVERED)) table[i][j].setEnabled(false);
		table[i][j].setBackground(c);
	}

	
	private void privatePaint(int i, int j) {
		Color c=null;
		if (matrixShip[i][j]==null) c=colorMap.get(WATER);
		  else {
			 try {
				if (matrixShip[i][j].getStatus(i,j)==Ship.HIT)  c=colorMap.get(HIT_SHIP);
				 else c=colorMap.get(OPERATIVE_SHIP);
			} catch (Bad_Coordinate e) {}
		  }
		table[i][j].setBackground(c);
	}
	
	
	public boolean putShip(Ship ship, Coordinate shipCoordinate) throws Bad_Coordinate{
    	boolean b=super.putShip(ship, shipCoordinate);
    	repaint();
		return b;
   }
	
	
	public boolean hitTable(Coordinate coord) throws Bad_Coordinate{
		   boolean b=super.hitTable(coord);
		   repaint();
		   return b;
	   }

	  public void publicSetState(Coordinate coord, int state) {
		  System.out.println("SETOVAO BOJU " + coord);
		 if (state==COVERED) table[coord.getRow()][coord.getColumn()].setEnabled(true);
		 super.publicSetState(coord, state);
		 repaint();
	  }
	  
	  public String randomLayout(String shipLayout){
		 String s=super.randomLayout(shipLayout);
		 repaint();
		 return s;
	}

	  public Coordinate getLastPressed(){
		  return lastPressed;
	  }
	  
	  public void resetLastPressed(){
		  lastPressed=null;
	  }

	  
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (frame.state==MyFrame.DEPLOY_SHIPS_STATE){
		if (lastPressed!=null){
			int i=lastPressed.getRow();
			int j=lastPressed.getColumn();
			if (visible==PRIVATE) privatePaint(i,j);
			else publicPaint(i,j);
		}
		lastPressed=Coordinate.makeCoordinate(e.getActionCommand().split(" ")[0]);
		((JButton)e.getSource()).setBackground(Color.PINK);
	
	   }
	}

	public void addFireListener(firePanel firePanel) {
		
		for(int i=0;i<numRow();i++){
			for(int j=0;j<numCol();j++){
				table[i][j].addActionListener(firePanel);
			}
		}
		
	}
}
