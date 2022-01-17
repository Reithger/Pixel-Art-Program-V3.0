package visual.drawboard.corkboard;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;

import control.InputHandler;
import control.code.CodeInfo;
import control.code.CodeReference;
import misc.Canvas;
import visual.composite.HandlePanel;
import input.CustomEventReceiver;
import input.manager.actionevent.KeyActionEvent;

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
	
	private final static int[] NAME_BAR_CODES = new int[] {CodeReference.CODE_CLOSE_THING, CodeReference.CODE_MAXIMIZE_CANVAS};
	protected static final int HEADER_HEIGHT = 30;
	protected static final int SIDEBAR_WIDTH = 46;
	protected final static int MINIMUM_SIZE = 150;
	protected final static int CONTENT_X_BUFFER = 1;
	protected final static int CONTENT_Y_BUFFER = HEADER_HEIGHT + 1;
	private final static Font HOVER_TEXT_FONT = new Font("Serif", Font.BOLD, 16);

	
	public final static int CODE_CHECK_POSITION = 55;
	private final static Font TITLE_FONT = new Font("Serif", Font.BOLD, 16);
	protected final static int OVERLAY_INTERACT_CODE = -42;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String panelName;
	private HandlePanel panel;
	private ArrayList<CodeInfo> buttons;
	private HashSet<Integer> buttonCodes;
	private InputHandler reference;
	private boolean mutex;
	private static boolean contentLocked;
	private int zoom;
	private static boolean displayTooltip;
	private boolean toggleButtons;

//---  Constructors   -------------------------------------------------------------------------
	
	public Corkboard(String inNom, String inPanel, int inWidth, int inHeight) {
		toggleButtons = true;
		setName(inNom);
		setPanelName(inPanel);
		buttons = new ArrayList<CodeInfo>();
		buttonCodes = new HashSet<Integer>();
		displayTooltip = true;
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
			
			private boolean canvasDrag;
			
			private volatile int drawCounter;
			private volatile boolean mutexHere;
			
			private long tooltipWaitTime;
			
			private void openLockHere() {
				while(mutexHere) {}
				mutexHere = true;
			}
			
			private void closeLockHere() {
				mutexHere = false;
			}
			
			@Override
			public void clickEvent(int code, int x, int y, int clickStart) {
				switch(code) {
					case CodeReference.CODE_INTERACT_CONTENT :
						if(!contentLocked) {
							processDrawing(x, y);
						}
						break;
					case CodeReference.CODE_TOGGLE_CORKBOARD_BUTTONS:
						toggleButtons = !toggleButtons;
						updatePanel();
						break;
					default:
						onClick(code, x, y);
						getReference().handleCodeInput(code, getName());
						break;
				}
			}
			
			@Override
			public void clickPressEvent(int code, int x, int y, int clickStart) {
				drawCounter = 0;
				lastX = x;
				lastY = y;
				if(code == CodeReference.CODE_HEADER) {
					draggingHeader = true;
				}
				else if(code == CodeReference.CODE_RESIZE) {
					draggingResize = true;
				}
				else if(code == CodeReference.CODE_INTERACT_CONTENT) {
					processDrawing(x, y);
				}
				onClickPress(code, x, y);
			}
			
			@Override
			public void clickReleaseEvent(int code, int x, int y, int clickStart) {
				if(draggingResize) {
					resizePanel(getPanel().getWidth() + (x - lastX), getPanel().getHeight() + (y - lastY));
				}
				canvasDrag = false;
				draggingResize = false;
				draggingHeader = false;
				drawCounter = -1;
				if(code == CodeReference.CODE_INTERACT_CONTENT) {
					processDrawing(x, y);
				}
				onClickRelease(code, x, y);
			}
						
			@Override
			public void dragEvent(int code, int x, int y, int clickStart) {
				processDragging(x, y);
				if((code == CodeReference.CODE_INTERACT_CONTENT || canvasDrag) && !draggingResize) {
					if(!contentLocked) {
						processDrawing(x, y);
					}
					else {
						canvasDrag = true;
						hand.setOffsetX("move", hand.getOffsetX("move") + (x - lastX));
						hand.setOffsetY("move", hand.getOffsetY("move") + (y - lastY));
						lastX = x;
						lastY = y;
					}
				}
				else if(code != OVERLAY_INTERACT_CODE){
					drawCounter = -1;
				}
				onDrag(code, x, y);
			}
			
			@Override
			public void mouseMoveEvent(int code, int x, int y) {
				if(displayTooltip && (buttonCodes.contains(code) || code == CodeReference.CODE_RESIZE)) {
					if(tooltipWaitTime == -1) {
						tooltipWaitTime = System.currentTimeMillis();
					}
					else if(System.currentTimeMillis() - tooltipWaitTime > 100) {
						String disp = CodeReference.getCodeLabel(code);
						int posX = x;
						int posY = y;
						int wid = hand.getTextWidth(disp, HOVER_TEXT_FONT) + 3;
						int hei = hand.getTextHeight(HOVER_TEXT_FONT);
						posX = ((posX - wid) < 0 ? posX : (posX - wid));
						posY = ((posY - hei) < 0 ? posY : (posY - hei));
						posX += (posX == x) ? 15 : 0;
						hand.addText("tooltip_overlay_txt", 100, "move", posX, posY, wid, hei, disp, HOVER_TEXT_FONT, true, true, false);
						hand.addRectangle("tooltip_overlay_rect", 99, "move", posX + wid / 2, posY + hei / 2, wid + 4, hei, true, Color.white, Color.black);
					
					}
				}
				else {
					tooltipWaitTime = -1;
					hand.removeElementPrefixed("tooltip");
				}
			}
			
			@Override
			public void keyReleaseEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_UP);
				
			}
			
			@Override
			public void keyPressEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_DOWN);
			}
			
			@Override
			public void keyEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY);
			}
			
			private void processDrawing(int x, int y) {
				openLockHere();
				int actX = x - hand.getOffsetX("move") - CONTENT_X_BUFFER;
				int actY = y - hand.getOffsetY("move") - CONTENT_Y_BUFFER;
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
		drawTitle();
	}

	public void updatePanel() {
		HandlePanel p = getPanel();
		int wid = p.getWidth();
		int hei = p.getHeight();
		p.removeElementPrefixed("thck");
		p.handleThickRectangle("thck", "no_move", 10, 0, HEADER_HEIGHT, wid,hei, Color.black, 2);
		
		int size = 24;
		
		int posX = wid - size;
		int posY = HEADER_HEIGHT + size;

		drawButtons(posX, posY, hei, size);
		
		posY = hei - size;
		
		p.handleImageButton("imgB", "no_move", 15, posX, posY, size, size, CodeReference.getCodeImagePath(CodeReference.CODE_RESIZE), CodeReference.CODE_RESIZE);
		
		drawTitle();
		
		int rA = CONTENT_X_BUFFER + getContentWidth();
		int lA = CONTENT_X_BUFFER;
		int tA = CONTENT_Y_BUFFER;
		int bA = CONTENT_Y_BUFFER + getContentHeight();
		p.addLine("line1", 5, "move", rA, tA, rA, bA, 1, Color.black);
		p.addLine("line2", 5, "move", lA, bA, rA, bA, 1, Color.black);
		p.addLine("line3", 5, "move", lA, tA, lA, bA, 1, Color.black);
		p.addLine("line4", 5, "move", lA, tA, rA, tA, 1, Color.black);
		p.handleThickRectangle("thck", "no_move", 10, 0, HEADER_HEIGHT, wid, hei, Color.black, 2);
		updatePanelLocal();
	}

	private void drawTitle() {
		getPanel().removeElementPrefixed("name_bar_title");
		int butHei = HEADER_HEIGHT * 9/10;
		int texWid = getPanel().getTextWidth(getName(), TITLE_FONT) * 2;
		if(texWid < 0) {
			texWid = getWidth() - butHei * (NAME_BAR_CODES.length + 1);
			texWid = texWid < 20 ? 20 : texWid;
		}
		getPanel().handleTextButton("name_bar_title", "no_move", 15, texWid / 2, butHei / 2, texWid, butHei, TITLE_FONT, getName(), CodeReference.CODE_HEADER, Color.white, Color.black);
		
		for(int i = 0 ; i < NAME_BAR_CODES.length; i++) {
			getPanel().handleImageButton("name_bar_button_" + i, "no_move", 15, getWidth() - (int)((i + .5) * butHei) - 3, butHei / 2, butHei, butHei, CodeReference.getCodeImagePath(NAME_BAR_CODES[i]), NAME_BAR_CODES[i]);
		}
	}
	
	private void drawButtons(int posX, int posY, int hei, int size) {
		HandlePanel p = getPanel();
		p.removeElementPrefixed("button");
		
		ArrayList<CodeInfo> butt = toggleButtons ? buttons : new ArrayList<CodeInfo>();
		
		int displaceY = size * 3 / 2;
		int displaceX = size * 5 / 4;
		int vert = hei / displaceY - 2;
		int colum = butt.size() / vert + (butt.size() % vert == 0 ? 0 : 1);
		int useY = posY;
		posX -= displaceX * (colum);
		
		CodeInfo bI = CodeReference.getCodeInfo(CodeReference.CODE_TOGGLE_CORKBOARD_BUTTONS);
		p.handleImageButton("button_" + bI.getLabel(), "no_move", 15, posX, useY, size, size, bI.getImagePath(), bI.getCode());
		
		for(int i = colum - 1; i >= 0; i--) {
			useY = posY;
			for(int j = 0; j < vert; j++) {
				if(i * vert + j >= butt.size()) {
					break;
				}
				bI = butt.get(i * vert + j);
				p.handleImageButton("button_" + bI.getLabel(), "no_move", 15, posX + displaceX * (colum - i), useY, size, size, bI.getImagePath(), bI.getCode());
				useY += displaceY;
			}
		}
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
		buttonCodes = new HashSet<Integer>();
		for(CodeInfo ci : in) {
			buttonCodes.add(ci.getCode());
		}
	}
	
	public void addButton(CodeInfo bI) {
		buttons.add(bI);
		buttonCodes.add(bI.getCode());
	}
	
	public void removeButton(String nom) {
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).getLabel().equals(nom)) {
				buttonCodes.remove(buttons.get(i).getCode());
				buttons.remove(i);
				return;
			}
		}
	}
	
	public void removeAllButtons() {
		buttons.clear();
		buttonCodes.clear();
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
	
	public void setContentLocked(boolean set) {
		contentLocked = set;
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
