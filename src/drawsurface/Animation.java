package drawsurface;

import java.awt.Image;
import java.util.ArrayList;

import visual.panel.CanvasPanel;
import meta.HandlePanel;

public class Animation implements Display{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SAVE_TYPE_GIF = 0;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Picture> frames;
	private double scale;
	private int activeFrame;
	private int period;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Animation() {
		frames = new ArrayList<Picture>();
		scale = 1;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public HandlePanel generateDisplay(int x, int y) {
		int wid = (int)(scale * frames.get(0).getWidth());
		int hei = (int)(scale * frames.get(0).getHeight());
		HandlePanel p = new HandlePanel(x, y, wid, hei) {
			@Override
			public void keyBehaviour(char event) {
				//TODO: Keyboard shortcuts for Animation
			}
		};
		Image[] disp = new Image[frames.size()];
		for(int i = 0; i < disp.length; i++) {
			disp[i] = frames.get(i).generateImage();
		}
		p.addAnimation("anim", 10, false, 0, 0, false, period, scale, disp);
		return p;
	}

	@Override
	public CanvasPanel generateCanvas(int x, int y) {
		return frames.get(activeFrame).generateCanvas(x, y);
	}
	
	public void moveFramePosition(int a, int b) {
		Picture hold = frames.get(a);
		frames.remove(a);
		frames.add(b > a ? b - 1 : b, hold);
	}

	public void saveAnimation(String filePath, int type) {
		//TODO:
	}
	
	public void export(String filePath) {
		//TODO: encode so can be read back in; might just be generating a folder of composite images?
	}
	
	public void importAnimation(String filePath) {
		//TODO: Read encoded data to generate Animation
	}
	
	//-- Animation Frames  ------------------------------------
	
	public void removeAnimationFrame(int ind) {
		frames.remove(ind);
	}
	
	public void addAnimationFrame(Picture p) {
		frames.add(p);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public ArrayList<Picture> getAnimationFrames(){
		return frames;
	}
	
}
