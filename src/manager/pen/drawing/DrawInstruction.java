package manager.pen.drawing;

import java.awt.Color;

import manager.curator.picture.LayerPicture;

public class DrawInstruction {

	private int x;
	private int y;
	private Color col;
	private int layer;
	private LayerPicture ref;
	
	public DrawInstruction(int inX, int inY, Color inCol, int inLayer, LayerPicture inRef) {
		x = inX;
		y = inY;
		col = inCol;
		layer = inLayer;
		ref = inRef;
	}
	
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
	
	public LayerPicture getPicture() {
		return ref;
	}
	
}
