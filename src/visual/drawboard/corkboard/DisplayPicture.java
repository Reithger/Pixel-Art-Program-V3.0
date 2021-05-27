package visual.drawboard.corkboard;

import control.CodeReference;
import misc.Canvas;

public class DisplayPicture extends Corkboard{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Canvas disp;
	private Canvas overlay;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(String nom, String inPanel, Canvas inImg) {
		super(nom, inPanel, inImg.getCanvasZoomWidth(), inImg.getCanvasZoomHeight());
		disp = inImg;
		updatePanel();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void updateImages(Canvas[] imgs) {
		disp = imgs[0];
		if(imgs.length > 1)
			overlay = imgs[1];
		updatePanel();
	}

	@Override
	public Corkboard duplicate(String nom, String panelName) {
		Corkboard c = new DisplayPicture(nom, panelName, disp);
		c.setReference(this.getReference());
		return c;
	}

	@Override
	protected void updatePanelLocal() {
		getPanel().removeElementPrefixed("display_picture_img");
		int usX = CONTENT_X_BUFFER;
		int usY = CONTENT_Y_BUFFER;
		getPanel().addCanvas("display_picture_img", 5, false, usX, usY, getContentWidth(), getContentHeight(), disp, CodeReference.CODE_INTERACT_CONTENT);
		if(overlay != null)
			getPanel().addCanvas("display_picture_img2", 3, false, usX, usY, getContentWidth(), getContentHeight(), overlay, OVERLAY_INTERACT_CODE);
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

	@Override
	public int getContentWidth() {
		return disp.getCanvasZoomWidth();
	}

	@Override
	public int getContentHeight() {
		return disp.getCanvasZoomHeight();
	}

}
