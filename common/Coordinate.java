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
public class Coordinate 
{
    private int row;
    private int column;
    
    public Coordinate(int _row, int _column)
    {
        row = _row; column = _column; 
    }
    
    public static Coordinate makeCoordinate(String coordinateText){
		// making coordinate from coordinateText
    	int row=Integer.parseInt(coordinateText.substring(0,2),10);
    	int column=Integer.parseInt(coordinateText.substring(2,4),10);
    	return new Coordinate(row,column);
    }
    
	public int getColumn() {
		return column;
	}

	
	public boolean inRange(int size){
		if (row<0 || column<0 || row>size || column>size) return false;
		else return true;
	}
	
	public String toString(){
    	String s="";
    	if (row<10) s+='0';
    	s+=row;
    	if (column<10) s+='0';
    	return s+column;
    }

	public int getRow() {	
		return row;
	}
	
	
}
