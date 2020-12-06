package visual.drawboard.corkboard;

import control.CodeReference;
import control.InputHandler;
import misc.Canvas;

public class DisplayPicture extends Corkboard{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Canvas disp;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DisplayPicture(String nom, String inPanel, Canvas inImg, InputHandler ref) {
		super(nom, inPanel, inImg.getCanvasZoomWidth(), inImg.getCanvasZoomHeight(), ref);
		disp = inImg;
		updatePanel();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void updateImages(Canvas[] imgs) {
		disp = imgs[0];
		updatePanel();
	}

	@Override
	public Corkboard duplicate(String nom, String panelName) {
		Corkboard c = new DisplayPicture(nom, panelName, disp, getReference());
		return c;
	}

	@Override
	protected void updatePanelLocal() {
		getPanel().removeElement("img");
		int usX = CONTENT_X_BUFFER;
		int usY = CONTENT_Y_BUFFER;
		getPanel().addCanvas("img", 2, false, usX, usY, getContentWidth(), getContentHeight(), disp, CodeReference.CODE_INTERACT_CONTENT);
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
