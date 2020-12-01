package visual.drawboard.corkboard;

import java.awt.Color;
import java.awt.image.BufferedImage;

import control.CodeReference;
import misc.Canvas;
import visual.composite.HandlePanel;
import visual.drawboard.DrawingPage;

public abstract class Corkboard {

	/*
	 * Generically, any Corkboard allows:
	 *  - Lock the 'content' so you can't draw/drag it to move around
	 *  - Zoom in/out
	 *  - Reset position
	 *  - Move panel
	 *  - Resize panel
	 *  - See layer options (updateImages needs more meta-data?)
	 *  - Edit/navigate layers options
	 * 
	 */
	
//---  Constants   ----------------------------------------------------------------------------
	
	protected static final int HEADER_HEIGHT = 30;
	protected static final int SIDEBAR_WIDTH = 30;
	protected final static int MINIMUM_SIZE = 150;
	protected final static int CONTENT_X_BUFFER = 1;
	protected final static int CONTENT_Y_BUFFER = HEADER_HEIGHT + 1;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String panelName;
	private HandlePanel panel;
	private DrawingPage reference;
	private boolean mutex;
	private boolean contentLocked;
	private int zoom;

//---  Constructors   -------------------------------------------------------------------------
	
	public Corkboard(String inNom, String inPanel, int inWidth, int inHeight, DrawingPage ref) {
		setName(inNom);
		setPanelName(inPanel);
		setReference(ref);
		mutex = false;
		int wid = inWidth < MINIMUM_SIZE ? MINIMUM_SIZE : inWidth;
		int hei = inHeight < MINIMUM_SIZE ? MINIMUM_SIZE : inHeight;
		zoom = 1;
		generatePanel(wid, hei);
	}
	
//---  Operations   ---------------------------------------------------------------------------

	protected void generatePanel(int width, int height) {
		HandlePanel hand = new HandlePanel(0, 0, width + SIDEBAR_WIDTH, height + HEADER_HEIGHT) {
			
			private int lastX;
			private int lastY;	
			
			private boolean draggingHeader;
			private boolean draggingResize;
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				switch(code) {
					case CodeReference.CODE_INTERACT_CONTENT :
						if(!contentLocked) {
							processDrawing(x, y);
						}
						break;
					default:
						onClick(code, x, y);
						getReference().passOnCode(code, x, y, getName());
						break;
				}
			}
			
			@Override
			public void clickPressBehaviour(int code, int x, int y) {
				if(code == CodeReference.CODE_HEADER) {
					draggingHeader = true;
					lastX = x;
					lastY = y;
				}
				else if(code == CodeReference.CODE_RESIZE) {
					draggingResize = true;
					lastX = x;
					lastY = y;
				}
				onClickPress(code, x, y);
			}
			
			@Override
			public void clickReleaseBehaviour(int code, int x, int y) {
				if(draggingResize) {
					resizePanel(getPanel().getWidth() + (x - lastX), getPanel().getHeight() + (y - lastY));
				}
				draggingResize = false;
				draggingHeader = false;
				onClickRelease(code, x, y);
			}
			
			
			@Override
			public void dragBehaviour(int code, int x, int y) {
				processDragging(x, y);
				if(code == CodeReference.CODE_INTERACT_CONTENT) {
					if(!contentLocked) {
						processDrawing(x, y);
					}
					else {
						setOffsetX(getOffsetX() + (lastX - x));
						setOffsetY(getOffsetY() + (lastY - y));
						lastX = x;
						lastY = y;
					}
				}
				onDrag(code, x, y);
			}
			
			private void processDrawing(int x, int y) {
				int actX = x + getOffsetX() - CONTENT_X_BUFFER;
				int actY = y + getOffsetY() - CONTENT_Y_BUFFER;
				System.out.println(actX + " " + actY);
				getReference().passOnDraw(actX, actY, getName());
			}
			
			private void processDragging(int x, int y) {
				if(draggingHeader) {
					move(x - lastX, y - lastY);
				}
				else if(draggingResize) {
					//TODO: Transparent panel showing new corner size roughly
				}
			}
		};
		setPanel(hand);
		getPanel().setScrollBarHorizontal(false);
		getPanel().setScrollBarVertical(false);
		setTitle();
	}

	private void setTitle() {
		getPanel().removeElementPrefixed("texB");
		int butWid = getWidth() * 9/10;
		int butHei = HEADER_HEIGHT * 9/10;
		getPanel().handleTextButton("texB", true, butWid / 2, butHei / 2, butWid, butHei, DrawingPage.DEFAULT_FONT, getName(), CodeReference.CODE_HEADER, Color.white, Color.black);
	
	}
	
	public void updatePanel() {
		getPanel().removeElementPrefixed("thck");
		getPanel().handleThickRectangle("thck", true, 0, HEADER_HEIGHT, getPanel().getWidth(), getPanel().getHeight(), Color.black, 2);
		int size = 16;
		
		getPanel().handleImageButton("zoomIn", true, getPanel().getWidth() - size, HEADER_HEIGHT + size, size, size, "/assets/placeholder.png", CodeReference.CODE_INCREASE_ZOOM);
		getPanel().handleImageButton("zoomOut", true, getPanel().getWidth() - size, HEADER_HEIGHT + (int)(2.5 * size), size, size, "/assets/placeholder.png", CodeReference.CODE_DECREASE_ZOOM);
		
		getPanel().handleImageButton("imgB", true, getPanel().getWidth() - size, getPanel().getHeight() - size, size, size, "/assets/placeholder.png", CodeReference.CODE_RESIZE);
		setTitle();
		
		int rA = CONTENT_X_BUFFER + getContentWidth();
		int tA = CONTENT_Y_BUFFER;
		int bA = CONTENT_Y_BUFFER + getContentHeight();
		getPanel().addLine("line1", 5, true, rA, tA, rA, bA, 1, Color.black);
		getPanel().addLine("line2", 5, true, 0, bA, rA, bA, 1, Color.black);
		getPanel().handleThickRectangle("thck", true, 0, HEADER_HEIGHT, getPanel().getWidth(), getPanel().getHeight(), Color.black, 2);
		updatePanelLocal();
	}

	public void resizePanel(int wid, int hei) {
		wid = wid < MINIMUM_SIZE ? MINIMUM_SIZE : wid;
		hei = hei < MINIMUM_SIZE ? MINIMUM_SIZE : hei;
		getPanel().resize(wid, hei);
		resizePanelLocal(wid, hei);
		updatePanel();
	}

	public void move(int x, int y) {
		int newX = x + getPanel().getPanelXLocation();
		int newY = y + getPanel().getPanelYLocation();
		newX = newX < reference.getOriginX() ? reference.getOriginX() : newX;
		newX = newX > (reference.getOriginX() + reference.getWidth()) ? (reference.getOriginX() + reference.getWidth() - 10) : newX;
		newY = newY < reference.getOriginY() ? reference.getOriginY() : newY;
		newY = newY > (reference.getOriginY() + reference.getHeight()) ? (reference.getOriginY() + reference.getHeight() - 10) : newY;
		setLocation(newX, newY);
	}

	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}
	
	//-- Abstract  --------------------------------------------
	
	protected abstract void updatePanelLocal();

	public abstract void updateImages(Canvas[] imgs);
	
	public abstract Corkboard duplicate(String nom, String panelName);
	
	protected abstract void resizePanelLocal(int wid, int hei);

	protected abstract void onClick(int code, int x, int y);
	
	protected abstract void onClickPress(int code, int x, int y);
	
	protected abstract void onClickRelease(int code, int x, int y);
	
	protected abstract void onDrag(int code, int x, int y);
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setZoom(int in) {
		zoom = in < 1 ? 1 : in;
		updatePanel();
	}
	
	public void setReference(DrawingPage ref) {
		reference = ref;
	}
	
	public void setPanelName(String in) {
		panelName = in;
	}
	
	public void setLocation(int x, int y) {
		getPanel().setLocation(x, y);
	}

	public void setName(String in) {
		name = in;
	}
	
	public void setPanel(HandlePanel in) {
		panel = in;
	}
	
	public void toggleContentLocked() {
		contentLocked = !contentLocked;
	}

//---  Getter Methods   -----------------------------------------------------------------------

	public DrawingPage getReference() {
		return reference;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public boolean getContentLocked() {
		return contentLocked;
	}
	
	public int getWidth() {
		return getPanel().getWidth();
	}

	public int getHeight() {
		return getPanel().getHeight();
	}

	public abstract int getContentWidth();
	
	public abstract int getContentHeight();
	
	public String getName() {
		return name;
	}
	
	public HandlePanel getPanel() {
		return panel;
	}
	
	public String getPanelName() {
		return panelName;
	}

}