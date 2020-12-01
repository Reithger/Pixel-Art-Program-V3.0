package manager.pen;

import java.awt.Color;

public class Changes{
		
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	private Color[][] cols;
	private int layer;
	private String name;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Changes(String nom, int inLay) {
		name = nom;
		layer = inLay;
		cols = null;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addChange(int xIn, int yIn, Color[][] colsIn) {
		if(cols == null) {
			x = xIn;
			y = yIn;
			cols = colsIn;
			return;
		}
		int minX = xIn < x ? xIn : x;
		int minY = yIn < y ? yIn : y;
		int maxX = xIn + colsIn.length > x + cols.length ? xIn + colsIn.length : x + cols.length;
		int maxY = yIn + colsIn[0].length > y + cols[0].length ? yIn + colsIn.length : y + cols[0].length;
		int wid = maxX - minX;
		int hei = maxY - minY;
		if(wid != cols.length || hei != cols[0].length) {
			Color[][] newCol = new Color[wid][hei];
			for(int i = x - minX; i < cols.length; i++) {
				for(int j =  y - minY; j < cols[i].length; j++) {
					newCol[i - (x - minX)][j - (y - minY)] = cols[i][j];
				}
			}
			cols = newCol;
			x = minX;
			y = minY;
		}
		
		for(int i = 0; i < colsIn.length; i++) {
			for(int j = 0; j < colsIn[i].length; j++) {
				int usX = xIn - x + i;
				int usY = yIn - y + j;
				if(colsIn[i][j] != null && usX < cols.length && usY < cols[usX].length)
					cols[usX][usY] = colsIn[i][j];
			}
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public String getName() {
		return name;
	}
	
	public Color[][] getColors(){
		return cols;
	}

}
