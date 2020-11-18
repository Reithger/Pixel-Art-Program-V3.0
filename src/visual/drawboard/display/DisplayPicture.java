package visual.drawboard.display;

import java.awt.Color;
import java.awt.Image;

import visual.composite.HandlePanel;
import visual.composite.ImageDisplay;
import visual.drawboard.DrawingPage;

public class DisplayPicture implements Display{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HandlePanel p;
	private ImageDisplay iD;
	private String name;
	private DrawingPage reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(String nom, int wid, int hei, Image inImg, DrawingPage ref) {
		reference = ref;
		generateDisplay(wid, hei);
		name = nom;
		iD = new ImageDisplay(inImg, p);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public void generateDisplay(int width, int height) {
		p = new HandlePanel(0, 0, width, height + HEADER_HEIGHT) {
			@Override
			public void clickPressBehaviour(int code, int x, int y) {
				if(code == DrawingPage.CODE_HEADER_HOLD) {
					reference.passOnCode(DrawingPage.CODE_HEADER_PRESS, x, y, name);
				}
			}
			
			@Override
			public void clickReleaseBehaviour(int code, int x, int y) {
				if(code == DrawingPage.CODE_HEADER_HOLD) {
					reference.passOnCode(DrawingPage.CODE_HEADER_RELEASE, x, y, name);
				}
			}
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				reference.passOnCode(code, x, y, name);
			}
			
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Animation
			}
		};
		p.handleTextButton("texB", true, 0, 0, width, HEADER_HEIGHT, DrawingPage.DEFAULT_FONT, name, DrawingPage.CODE_HEADER_HOLD, Color.white, Color.black);
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
