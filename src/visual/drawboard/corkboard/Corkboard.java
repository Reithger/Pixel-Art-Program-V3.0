package visual.drawboard.corkboard;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import control.CodeInfo;
import control.CodeReference;
import control.InputHandler;
import misc.Canvas;
import visual.composite.HandlePanel;
import input.CustomEventReceiver;

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
	protected static final int SIDEBAR_WIDTH = 46;
	protected final static int MINIMUM_SIZE = 150;
	protected final static int CONTENT_X_BUFFER = 1;
	protected final static int CONTENT_Y_BUFFER = HEADER_HEIGHT + 1;
	
	public final static int CODE_CHECK_POSITION = 55;
	private final static Font TITLE_FONT = new Font("Serif", Font.BOLD, 16);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String panelName;
	private HandlePanel panel;
	private ArrayList<CodeInfo> buttons;
	private InputHandler reference;
	private boolean mutex;
	private boolean contentLocked;
	private int zoom;

//---  Constructors   -------------------------------------------------------------------------
	
	public Corkboard(String inNom, String inPanel, int inWidth, int inHeight) {
		setName(inNom);
		setPanelName(inPanel);
		buttons = new ArrayList<CodeInfo>();
		mutex = false;
		int wid = inWidth < MINIMUM_SIZE ? MINIMUM_SIZE : inWidth;
		int hei = inHeight < MINIMUM_SIZE ? MINIMUM_SIZE : inHeight;
		zoom = 1;
		generatePanel(wid, hei);
	}
	
//---  Operations   ---------------------------------------------------------------------------

	protected void generatePanel(int width, int height) {
		HandlePanel hand = new HandlePanel(0, 0, width + SIDEBAR_WIDTH, height + HEADER_HEIGHT);
		hand.setEventReceiver(new CustomEventReceiver() {
			
			private int lastX;
			private int lastY;	
			
			private boolean draggingHeader;
			private boolean draggingResize;
			
			private volatile int drawCounter;
			private volatile boolean mutexHere;
			
			private void openLockHere() {
				while(mutexHere) {}
				mutexHere = true;
			}
			
			private void closeLockHere() {
				mutexHere = false;
			}
			
			@Override
			public void clickEvent(int code, int x, int y) {
				switch(code) {
					case CodeReference.CODE_INTERACT_CONTENT :
						if(!contentLocked) {
							processDrawing(x, y);
						}
						break;
					default:
						onClick(code, x, y);
						getReference().handleCodeInput(code, getName());
						break;
				}
			}
			
			@Override
			public void clickPressEvent(int code, int x, int y) {
				lastX = x;
				lastY = y;
				if(code == CodeReference.CODE_HEADER) {
					draggingHeader = true;
				}
				else if(code == CodeReference.CODE_RESIZE) {
					draggingResize = true;
				}
				onClickPress(code, x, y);
			}
			
			@Override
			public void clickReleaseEvent(int code, int x, int y) {
				drawCounter = -1;
				if(draggingResize) {
					resizePanel(getPanel().getWidth() + (x - lastX), getPanel().getHeight() + (y - lastY));
				}
				draggingResize = false;
				draggingHeader = false;
				if(code == CodeReference.CODE_INTERACT_CONTENT) {
					processDrawing(x, y);
				}
				onClickRelease(code, x, y);
			}
						
			@Override
			public void dragEvent(int code, int x, int y) {
				processDragging(x, y);
				if(code == CodeReference.CODE_INTERACT_CONTENT) {
					if(!contentLocked) {
						processDrawing(x, y);
					}
					else {
						hand.setOffsetX(hand.getOffsetX() + (x - lastX));
						hand.setOffsetY(hand.getOffsetY() + (y - lastY));
						lastX = x;
						lastY = y;
					}
				}
				onDrag(code, x, y);
			}
			
			public void keyEvent(char code) {
				reference.handleKeyInput(code);
			}
			
			private void processDrawing(int x, int y) {
				openLockHere();
				int actX = x - hand.getOffsetX() - CONTENT_X_BUFFER;
				int actY = y - hand.getOffsetY() - CONTENT_Y_BUFFER;
				getReference().handleDrawInput(actX, actY, drawCounter++, getName());
				closeLockHere();
			}
			
			private void processDragging(int x, int y) {
				if(draggingHeader) {
					move(x - lastX, y - lastY);
				}
				else if(draggingResize) {
					//TODO: Transparent panel showing new corner size roughly
				}
			}
		});
		setPanel(hand);
		hand.getPanel().setBackground(null);
		getPanel().setScrollBarHorizontal(false);
		getPanel().setScrollBarVertical(false);
		drawTitle();
	}

	public void updatePanel() {
		HandlePanel p = getPanel();
		int wid = p.getWidth();
		int hei = p.getHeight();
		p.removeElementPrefixed("thck");
		p.handleThickRectangle("thck", true, 0, HEADER_HEIGHT, wid,hei, Color.black, 2);
		
		int size = 24;
		
		int posX = wid - size;
		int posY = HEADER_HEIGHT + size;

		
		for(int i = 0; i < buttons.size(); i++) {
			CodeInfo bI = buttons.get(i);
			p.handleImageButton(bI.getLabel(), true, posX, posY, size, size, bI.getImagePath(), bI.getCode());
			posY += 3 * size / 2;
		}
		
		posY = hei - size;
		
		p.handleImageButton("imgB", true, posX, posY, size, size, CodeReference.getCodeImagePath(CodeReference.CODE_RESIZE), CodeReference.CODE_RESIZE);
		
		drawTitle();
		
		int rA = CONTENT_X_BUFFER + getContentWidth();
		int lA = CONTENT_X_BUFFER;
		int tA = CONTENT_Y_BUFFER;
		int bA = CONTENT_Y_BUFFER + getContentHeight();
		p.addLine("line1", 5, false, rA, tA, rA, bA, 1, Color.black);
		p.addLine("line2", 5, false, lA, bA, rA, bA, 1, Color.black);
		p.addLine("line3", 5, false, lA, tA, lA, bA, 1, Color.black);
		p.addLine("line4", 5, false, lA, tA, rA, tA, 1, Color.black);
		p.handleThickRectangle("thck", true, 0, HEADER_HEIGHT, wid, hei, Color.black, 2);
		updatePanelLocal();
	}

	private void drawTitle() {
		getPanel().removeElementPrefixed("texB");
		int butWid = getWidth() * 9/10;
		int butHei = HEADER_HEIGHT * 9/10;
		getPanel().handleTextButton("texB", true, butWid / 2, butHei / 2, butWid, butHei, TITLE_FONT, getName(), CodeReference.CODE_HEADER, Color.white, Color.black);
		getPanel().handleImageButton("close", true, getWidth() * 19 / 20, butHei / 2, butHei, butHei, CodeReference.getCodeImagePath(CodeReference.CODE_CLOSE_THING), CodeReference.CODE_CLOSE_THING);
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
		setLocation(newX, newY);
		reference.handleCodeInput(CODE_CHECK_POSITION, name);
	}

	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}

	//-- Buttons  ---------------------------------------------
	
	public void assignCodeInfo(ArrayList<CodeInfo> in) {
		buttons = in;
	}
	
	public void addButton(CodeInfo bI) {
		buttons.add(bI);
	}
	
	public void removeButton(String nom) {
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).getLabel().equals(nom)) {
				buttons.remove(i);
				return;
			}
		}
	}
	
	public void removeAllButtons() {
		buttons.clear();
	}
	
	public void moveButton(int i, int j) {
		CodeInfo b = buttons.get(i);
		buttons.remove(i);
		buttons.add(j + (i < j ? -1 : 0), b);
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
	
	public void setReference(InputHandler ref) {
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

	public InputHandler getReference() {
		return reference;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public int getSidebarWidth() {
		return SIDEBAR_WIDTH;
	}
	
	public int getHeaderHeight() {
		return HEADER_HEIGHT;
	}
	
	public int getPositionX() {
		return getPanel().getPanelXLocation();
	}
	
	public int getPositionY() {
		return getPanel().getPanelYLocation();
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
