/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleships.common;
/**
 *
 * @author POOP
 */
public class Table
{
	public static final int COVERED=0;
	public static final int WATER=1;
	public static final int HIT_SHIP=2;
	public static final int OPERATIVE_SHIP=3;
	//matrix ship is use only for PRIVATE mode, that is a mode that are used for user table, all fields are undecovred
    protected Ship matrixShip[][];
	//matrix ship is use only for PUBLIC mode, that is a mode that are used for opponent table, all fields are covered on the beginning and will be undecovered in game 
    protected int matrixStatus[][];
	
    public static final int PRIVATE=0;
    public static final int PUBLIC=1;
 
    protected int visible;
    
    public Table(int rowNumber, int columnNumber, int status) throws NegativDimension{
    	visible=status;
    	if (rowNumber<=0 || columnNumber<=0) throw new NegativDimension();
		// if mode is private make only matrixShip
    	if (status==PRIVATE) matrixShip=new Ship[rowNumber][columnNumber];
    	// if mode is public make only markerStatus
    	else matrixStatus=new int[rowNumber][columnNumber];
    
    }
    
   public Table(int rowNumber, int columnNumber) throws NegativDimension{
	        this(rowNumber,columnNumber,PRIVATE);
    }
   
    public void deleteTable(){
    	if (visible==PRIVATE) matrixShip=new Ship[matrixShip.length][matrixShip[0].length];
    	else matrixStatus=new int[matrixStatus.length][matrixStatus[0].length];
    }
    	
    
    public Ship getShip(Coordinate shipCoordinate) throws Bad_Coordinate{
    	 System.out.println(toString());
    	if (visible==PRIVATE){
    		System.out.println("usao");
			// only private option, because in public mode we don't know value of covered field
	        if(!shipCoordinate.inRange(matrixShip.length)) throw new Bad_Coordinate();
	        System.out.println("usao" + shipCoordinate);
	    	return matrixShip[shipCoordinate.getRow()][shipCoordinate.getColumn()];
    	}
    	return null;
    }
    
    
    public boolean putShip(Ship ship, Coordinate shipCoordinate) throws Bad_Coordinate{
    	if (visible==PRIVATE){
    		
		if (shipCoordinate==null) return false;
		// first check coordinates range
        if (!shipCoordinate.inRange(matrixShip.length)) {
        	System.out.println("Nije u opsegu");
        	throw new Bad_Coordinate();
        }
        }
         ship.setFirstCoordinate(shipCoordinate);
         
		 // if we can put ship on this table, function spaceAvailable return true
         if (ship.spaceAvailable(this)){
        	 for(int i=0;i<ship.segmentNumber();i++){
        		 Coordinate coord=ship.coordinateOfSegment(i);
        		 matrixShip[coord.getRow()][coord.getColumn()]=ship;
        		 
        	 }
        	 System.out.println(toString());
        	 return true;
          }
         else{
        	 System.out.println("Nije space available");
         }
      return false;
   }
  
    public boolean putShip(Ship ship) throws Bad_Coordinate {
    	if (ship==null) return false;
    	return putShip(ship,ship.getFirstCoordinate());
   }

	public int numberOfOperativeSegments() {
		// count number of operative segments, this function will be used in server for check nuber of shoots that player can make
    	int counter=0;
    	if (visible==PRIVATE){
	    	for(int i=0;i<matrixShip.length;i++){
	    		for(int j=0;j<matrixShip[0].length;j++){
		    		try {
						if (matrixShip[i][j]!=null && matrixShip[i][j].getStatus(new Coordinate(i,j))==Ship.OPERATIV) counter++;
					} catch (Bad_Coordinate e) {}
	    		}
	    	}
    	}
    	return counter;
    }
    
    public int numRow() {
    	if (visible==PRIVATE) return matrixShip.length;
    	else return matrixStatus.length;
    }
    public int numCol() {
	   if (visible==PRIVATE) return matrixShip[0].length;
	   else  return matrixStatus[0].length;
    }
   
   public String toString(){
	   // make string of table
	  StringBuilder stringTable=new  StringBuilder("");
	  for(int i=0;i<numRow();i++){
		  for(int j=0;j<numCol();j++){
			  // add chars depends on mode 
			  if (visible==PRIVATE) stringTable.append(privatePrint(i,j));
			  else stringTable.append(publicPrint(i,j));
		  }
		  stringTable.append("\n");
	  }
	return stringTable.toString();
   }

   private char privatePrint(int i, int j){
	   // use private print where all field are visible to user
	   if (matrixShip[i][j]==null) return '~';
		  else {
			 try {
				if (matrixShip[i][j].getStatus(i,j)==Ship.HIT) return '*';
				 else return '#';
			} catch (Bad_Coordinate e) {}
		  }
	   return ' ';
   }
   
   private char publicPrint(int i, int j){
	   // use public print and follow state of opponent table
	   if(matrixStatus[i][j]==COVERED) return 'x';
	   else if (matrixStatus[i][j]==WATER) return '~';
	   else return  '*';
   }
   
   
   public boolean hitTable(Coordinate coord) throws Bad_Coordinate{
	   // 
	    if(visible==PRIVATE){
			// hit table is used only in private mode, this use server in fire mode
		Ship ship=getShip(coord);
		if(ship==null) return false;
		if (ship.getStatus(coord)==Ship.HIT) return false;
		ship.hitSegment(coord);
		return true;
	    }
	    return false;
   }

  public void publicSetState(Coordinate coord, int state) {
	  // this use battleships player to update state of opponent tables thanks to UPDATE message
	   matrixStatus[coord.getRow()][coord.getColumn()]=state;
  }
  
  public String randomLayout(String shipLayout){
	  // this use server to make random layout of ships 
	  // split string with this delimiters
	    String delims = "D()=[,];S"; // napravi!!!!
		String[] tokens = StringSpliter.delimStr(shipLayout, delims);
		StringBuilder newMessage = new StringBuilder("");
		for(int i = 0; i < tokens.length; i++){
			System.out.println(tokens[i]);
		}
		int sizeTable=Integer.parseInt(tokens[0]);
		for(int i=0;i<tokens.length/2;i++){
			int numberOfSegment=Integer.parseInt(tokens[2*i+1]);
			int numberOfShips=Integer.parseInt(tokens[2*i+2]);
			if(numberOfSegment <= 0 || numberOfShips <= 0 ) continue;
			for(int j=0;j<numberOfShips;j++){
				Ship ship=null;
				// random choose orientation of ship 
				if (Math.random()>0.5) ship=new Ship(numberOfSegment,Ship.HORIZONTAL);
				else ship=new Ship(numberOfSegment,Ship.HORIZONTAL);
				int row=(int)(Math.random()*sizeTable);
				int col=(int)(Math.random()*sizeTable);
				System.out.println("Row : " + row + " Col : " + col);
				// set first coordinate, using random coordinates
				ship.setFirstCoordinate(new Coordinate(row,col));
				try {
					// try to put this on table , if this failed , ignore this ship 
					if(putShip(ship)){
					System.out.println("Uspeo da stavi ");
					newMessage.append(ship.toString() + ";");
					}
				} catch (Bad_Coordinate e) {}
			}
		}
		return newMessage.toString();
  }

}
