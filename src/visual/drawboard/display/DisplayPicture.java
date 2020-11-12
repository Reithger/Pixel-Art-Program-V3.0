package visual.drawboard.display;

import java.awt.Image;

import visual.composite.HandlePanel;
import visual.composite.ImageDisplay;

public class DisplayPicture implements Display{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HandlePanel p;
	private ImageDisplay iD;
	private String name;

//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(String nom, int wid, int hei, Image inImg) {
		generateDisplay(wid, hei);
		name = nom;
		iD = new ImageDisplay(inImg, p);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public void generateDisplay(int width, int height) {
		p = new HandlePanel(0, 0, width, height);
	}
	
	public void updateDisplay(Image ... in) {
		iD.setImage(in[0]);
	}

	public void move(int x, int y) {
		int oldX = p.getPanelXLocation();
		int oldY = p.getPanelYLocation();
		setLocation(oldX + x, oldY + y);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setLocation(int x, int y) {
		p.setLocation(x,  y);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public HandlePanel getPanel() {
		return p;
	}
	
	public Image getImage() {
		return iD.getImage();
	}

	public int getWidth() {
		return p.getWidth();
	}
	
	public int getHeight() {
		return p.getHeight();
	}
	
}
