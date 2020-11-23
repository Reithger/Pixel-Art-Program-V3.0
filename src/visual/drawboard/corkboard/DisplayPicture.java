package visual.drawboard.corkboard;

import java.awt.Image;
import java.awt.image.BufferedImage;

import visual.composite.ImageDisplay;
import visual.drawboard.DrawingPage;

public class DisplayPicture extends Corkboard{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ImageDisplay iD;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(String nom, String inPanel, int wid, int hei, Image inImg, DrawingPage ref) {
		super(nom, inPanel, wid, hei, ref);
		iD = new ImageDisplay(inImg, getPanel());
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
