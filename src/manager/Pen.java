package manager;

import java.awt.Color;

import manager.component.picture.ArtPicture;

public class Pen {

	private Color col;
	
	public void setColor(Color in) {
		col = in;
	}
	
	public void draw(ArtPicture aP, int x, int y) {
		aP.setPixel(x, y, col);	//Most basic form of drawing
	}
	
}
