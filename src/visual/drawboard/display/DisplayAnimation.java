package visual.drawboard.display;

import java.awt.Color;
import java.awt.Image;

import visual.composite.HandlePanel;
import visual.drawboard.DrawingPage;

public class DisplayAnimation implements Display{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Image[] frames;
	private int activeFrame;	//TODO: Support for pausing, slowing, etc. the animation
	private int period;
	private double scale;
	private HandlePanel p;
	private String name;
	private String panelName;
	private DrawingPage reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayAnimation(String nom, String inPanel, int width, int height, Image[] inFrames, DrawingPage ref) {
		scale = 1;
		name = nom;
		panelName = inPanel;
		generateDisplay(width, height);
		reference = ref;
		updateDisplay(inFrames);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
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
	
	@Override
	public void updateDisplay(Image ... images) {
		frames = images;
		updateAnimation();
	}
	
	private void updateAnimation() {
		p.removeElement("anim");
		p.addAnimation("anim", 10, false, 0, 0, false, period, scale, frames);
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
	
	public String getPanelName() {
		return panelName;
	}
	
	public HandlePanel getPanel() {
		return p;
	}

	public int getWidth() {
		return p.getWidth();
	}
	
	public int getHeight() {
		return p.getHeight();
	}
	
}
