package visual.drawboard.corkboard;

import java.awt.Image;
import java.awt.image.BufferedImage;

import visual.drawboard.DrawingPage;

public class DisplayAnimation extends Corkboard{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Image[] frames;
	private int activeFrame;	//TODO: Support for pausing, slowing, etc. the animation
	private int period;
	private double scale;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayAnimation(String nom, String inPanel, int width, int height, BufferedImage[] inFrames, DrawingPage ref) {
		super(nom, inPanel, width, height, ref);
		updateImages(inFrames, 1);
	}

	
//---  Operations   ---------------------------------------------------------------------------

	@Override
	public void updateImages(BufferedImage[] imgs, int zoom) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public Corkboard duplicate(String nom, String panelName) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	protected void generatePanelLocal() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void updatePanelLocal() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void resizePanelLocal(int wid, int hei) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void onClick(int code, int x, int y) {
		// TODO Auto-generated method stub
	}
	

	@Override
	protected void onClickPress(int code, int x, int y) {
		// TODO Auto-generated method stub
	}
	

	@Override
	protected void onClickRelease(int code, int x, int y) {
		// TODO Auto-generated method stub
	}
	

	@Override
	protected void onDrag(int code, int x, int y) {
		// TODO Auto-generated method stub
	}

//---  Setter Methods   -----------------------------------------------------------------------

//---  Getter Methods   -----------------------------------------------------------------------

}
