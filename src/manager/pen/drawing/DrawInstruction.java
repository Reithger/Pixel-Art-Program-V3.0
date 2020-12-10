package manager.pen.drawing;

import java.awt.Color;

public class DrawInstruction {

//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	private Color col;
	private int layer;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawInstruction(int inX, int inY, Color inCol, int inLayer) {
		x = inX;
		y = inY;
		col = inCol;
		layer = inLayer;
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
	
	public Color getColor() {
		return col;
	}
	
}
