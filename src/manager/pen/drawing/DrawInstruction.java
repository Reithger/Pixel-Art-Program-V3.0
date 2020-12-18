package manager.pen.drawing;

import java.awt.Color;

public class DrawInstruction {

//---  Instance Variables   -------------------------------------------------------------------
	
	private String ref;
	private int x;
	private int y;
	private Color col;
	private int penMode;
	private int regionMode;
	private Color[][] cols;
	private int layer;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawInstruction(String inRef, int penM, int regionM, Color[][] use, int inX, int inY, Color inCol, int inLayer) {
		x = inX;
		y = inY;
		col = inCol;
		ref = inRef;
		penMode = penM;
		regionMode = regionM;
		cols = use;
		layer = inLayer;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getLayer() {
		return layer;
	}
	
	public String getReference() {
		return ref;
	}
	
	public int getPenMode() {
		return penMode;
	}
	
	public int getRegionMode() {
		return regionMode;
	}
	
	public Color[][] getColorArray(){
		return cols;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return col;
	}
	
}
