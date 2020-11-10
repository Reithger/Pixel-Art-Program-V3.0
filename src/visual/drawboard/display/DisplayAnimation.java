package visual.drawboard.display;

import java.awt.Image;

import visual.composite.HandlePanel;

public class DisplayAnimation implements Display{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SAVE_TYPE_GIF = 0;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Image[] frames;
	private double scale;
	private int activeFrame;
	private int period;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayAnimation(Image[] inFrames, int scaleIn) {
		frames = inFrames;
		scale = scaleIn;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public HandlePanel generateDisplay(int x, int y) {
		int wid = (int)(scale * frames[0].getWidth(null));
		int hei = (int)(scale * frames[0].getHeight(null));
		HandlePanel p = new HandlePanel(x, y, wid, hei) {
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Animation
			}
		};
		p.addAnimation("anim", 10, false, 0, 0, false, period, scale, frames);
		return p;
	}

}
