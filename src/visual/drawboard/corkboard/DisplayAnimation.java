package visual.drawboard.corkboard;

import java.awt.Image;
import java.awt.image.BufferedImage;

import misc.Canvas;
import visual.drawboard.DrawingPage;

public class DisplayAnimation extends Corkboard{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Image[] frames;
	private int activeFrame;	//TODO: Support for pausing, slowing, etc. the animation
	private int period;
	private double scale;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayAnimation(String nom, String inPanel, Canvas[] inFrames, DrawingPage ref) {
		super(nom, inPanel, inFrames[0].getCanvasZoomWidth(), inFrames[0].getCanvasZoomHeight(), ref);
		updateImages(inFrames);
	}

	
//---  Operations   ---------------------------------------------------------------------------

	@Override
	public void updateImages(Canvas[] imgs) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public Corkboard duplicate(String nom, String panelName) {
		// TODO Auto-generated method stub
		return null;
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


	@Override
	public int getContentWidth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getContentHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

//---  Setter Methods   -----------------------------------------------------------------------

//---  Getter Methods   -----------------------------------------------------------------------

}
