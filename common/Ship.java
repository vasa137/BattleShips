package battleships.common;

public class Ship {
    public static final int HORIZONTAL=0;
    public static final int VERTICAL=1;
    
    public static final int OPERATIV=0;
    public static final int HIT=1;

	private int [] segmentStatus;
	private int orientation;
	private Coordinate firstCoordinate;

	
	public Ship(int segmentNumber,int orient){
		segmentStatus = new int[segmentNumber];
		orientation=orient;
	}
	
	public void setFirstCoordinate(Coordinate first){
		firstCoordinate=first;
	}
	public int segmentNumber(){
	    return segmentStatus.length;
	}
	
	public Coordinate coordinateOfSegment(int index) throws Bad_Coordinate{
		//first index is 0
		// get one of segments depends of index value
		
		if (index<0 || index >= segmentStatus.length) throw new Bad_Coordinate();
		
		//if orientation is HORIZONTAL/VERTICAL just increase column/row value
		
		if (orientation==HORIZONTAL) return new Coordinate(firstCoordinate.getRow(),firstCoordinate.getColumn()+index);
    	else return new Coordinate(firstCoordinate.getRow()+index,firstCoordinate.getColumn());
	}
	
	
	public String toString(){
	// make string of format : "S(segmentNumber)=[Coordinate1,Coordinate2, ... ]"
		StringBuilder s=new StringBuilder("S("+segmentStatus.length +")=[");
		if (firstCoordinate!=null){
			
	      for(int i=0;i<segmentStatus.length;i++){
	        	try {
	        		if(i!=0) s.append(",");
					s.append(coordinateOfSegment(i));
				} catch (Bad_Coordinate e) {}
	      }
	          s.append("]");
		}
	    return s.toString();
	}
	
	public static Ship makeShip(String shipText){
		System.out.println( "ship Text " + shipText);
		if (shipText==null && shipText.equals("")) return null;
		String delims = "S[,]=()";
		String[] tokens = StringSpliter.delimStr(shipText, delims);
		// make ship depends on text of format: "S(segmentNumber)=[Coordinate1,Coordinate2 ... ]"
        int orientation=1;
		if (tokens.length>=3){
		   orientation=Integer.parseInt(tokens[2],10)-Integer.parseInt(tokens[1],10);
		}
		if (orientation==1) orientation=HORIZONTAL;
		else orientation=VERTICAL;
		Ship s=null;
		if (tokens.length>=1){
		System.out.println( "ship is  " + tokens[1] + "or < " +orientation);
		s=new Ship(Integer.parseInt(tokens[0]),orientation);
		s.firstCoordinate=Coordinate.makeCoordinate(tokens[1]);
		}
		return s;
	}
	
	
	public void hitSegment(Coordinate hitCoordinate) throws Bad_Coordinate{
		int index=-1;
		// first find index in array 
		
		if(orientation==HORIZONTAL && hitCoordinate.getRow() == firstCoordinate.getRow())
				index=hitCoordinate.getColumn()-firstCoordinate.getColumn();
		else if(orientation==VERTICAL && hitCoordinate.getColumn() == firstCoordinate.getColumn())
			index=hitCoordinate.getRow()-firstCoordinate.getRow();
		// then mark that field like hit
		if (index<0 || index>=segmentStatus.length) throw new Bad_Coordinate();
	     segmentStatus[index]=HIT;
	}
	
	
	public int getStatus(Coordinate hitCoordinate) throws Bad_Coordinate{
		int index=-1;
		// first find index in array 
		if(orientation==HORIZONTAL && hitCoordinate.getRow() == firstCoordinate.getRow())
				index=hitCoordinate.getColumn()-firstCoordinate.getColumn();
		else if(orientation==VERTICAL && hitCoordinate.getColumn() == firstCoordinate.getColumn())
			index=hitCoordinate.getRow()-firstCoordinate.getRow();
		
		//then return segment status(HIT OR OPERATIVE)
		if (index<0 || index>=segmentStatus.length) throw new Bad_Coordinate();
	     return segmentStatus[index];
	}

	public int getStatus(int i, int j) throws Bad_Coordinate{
		// get status using two ints
		return getStatus(new Coordinate(i,j));
	}
	
	
	public boolean spaceAvailable(Table table) {
		// forward table, and tries to put that ship on table
		
		if (firstCoordinate==null) {
			System.out.println("First coordinate is not set");
			return false;
		}
		
		for (int i=0;i<segmentStatus.length;i++){
			try {
				Coordinate coord=coordinateOfSegment(i);
				Ship s=table.getShip(coord);
				if (s!=null){
					System.out.println("1.Mesto je zauzeto " +coord );
					return false;
				}
				
				}
			 catch (Bad_Coordinate e) {
				System.out.println("Ne staje na tablu ");
	            return false;
			}
		}
		
		Coordinate lastCoordinate=null;
		
		//get last coordinate of ship
		try {
			lastCoordinate=coordinateOfSegment(segmentStatus.length-1);
		} catch (Bad_Coordinate e) {
			System.out.println("Space Available: Bad Coordinate");
		}
	
		// check all coordinates, all coordinates of ship and coordinates around that ship
		int xbegin=firstCoordinate.getRow()-1;
		int xend=lastCoordinate.getRow()+1;
		
		int ybegin=firstCoordinate.getColumn()-1;
		int yend=lastCoordinate.getColumn()+1;

		for(int i=xbegin;i<=xend;i++){
			for(int j=ybegin;j<=yend;j++){
				try {
					Ship s = table.getShip(new Coordinate(i,j));
					// get ship on set coordinate, if that place is unavailable , than we can return false
					if(s!=null) {
						System.out.println("2.Mesto je zauzeto " + i + j );
						return false;
					}
				} catch (Bad_Coordinate e) {
					System.out.println("Kad oivicim nece" + i + j );
				}
			}
		}
		return true;
	}

	public Coordinate getFirstCoordinate() {
		return firstCoordinate;
	}
}
