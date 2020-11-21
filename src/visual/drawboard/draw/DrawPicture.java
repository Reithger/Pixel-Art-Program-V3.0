package visual.drawboard.draw;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import control.CodeReference;
import misc.Canvas;
import visual.composite.HandlePanel;
import visual.drawboard.DrawingPage;
import visual.panel.element.DrawnCanvas;

public class DrawPicture implements Drawable{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int MINIMUM_SIZE = 150;
	private final static int CANVAS_CODE = 55;
	private final static int CODE_RESIZE = 54;
	private final static int CODE_HEADER = 53;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private HandlePanel handPanel;
	private Canvas canvas;
	private DrawnCanvas dCan;
	private DrawingPage reference;
	
	private boolean draggingHeader;
	private boolean draggingResize;
	
	private Image underlay;
	
	private Image overlay;
	
	private String panelName;
	
	private int lastX;
	
	private int lastY;
	
	private boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawPicture(String nom, String inPanel, int inWidth, int inHeight, DrawingPage ref) {
		reference = ref;
		panelName = inPanel;
		mutex = false;
		name = nom;
		int wid = inWidth < MINIMUM_SIZE ? MINIMUM_SIZE : inWidth;
		int hei = inHeight < MINIMUM_SIZE ? MINIMUM_SIZE : inHeight;
		canvas = new Canvas(inWidth, inHeight);
		generateCanvas(wid, hei);
	}
	
	public DrawPicture(String nom, String inPanel, BufferedImage in, DrawingPage ref) {
		reference = ref;
		panelName = inPanel;
		mutex = false;
		name = nom;
		int inWidth = in.getWidth(null);
		int inHeight = in.getHeight(null);
		int wid = inWidth < MINIMUM_SIZE ? MINIMUM_SIZE : inWidth;
		int hei = inHeight < MINIMUM_SIZE ? MINIMUM_SIZE : inHeight;
		canvas = new Canvas(in);
		generateCanvas(wid, hei);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void generateCanvas(int width, int height) {
		handPanel = new HandlePanel(0, 0, width, height + HEADER_HEIGHT) {
			@Override
			public void clickBehaviour(int code, int x, int y) {
				System.out.println(code);
				switch(code) {
					case CANVAS_CODE:
						int canX = x - dCan.getX();
						int canY = y - dCan.getY();
						if(canX >= 0 && canY >= 0){
							reference.passOnDraw(canX, canY, name);
						}
						break;
					case CODE_HEADER:
						break;
					case CODE_RESIZE:
						break;
					default:
						reference.passOnCode(code, x, y, name);
						break;
				}
			}
			
			@Override
			public void clickPressBehaviour(int code, int x, int y) {
				if(code == CODE_HEADER) {
					draggingHeader = true;
					lastX = x;
					lastY = y;
				}
				else if(code == CODE_RESIZE) {
					draggingResize = true;
					lastX = x;
					lastY = y;
				}
			}
			
			@Override
			public void clickReleaseBehaviour(int code, int x, int y) {
				if(draggingResize) {
					draggingResize = false;
					resizePanel(handPanel.getWidth() + (x - lastX), handPanel.getHeight() + (y - lastY));
				}
				if(code == CODE_HEADER) {
					draggingHeader = false;
				}

			}
			
			@Override
			public void dragBehaviour(int code, int x, int y) {
				if(draggingHeader) {
					move(x - lastX, y - lastY);
				}
				else if(draggingResize) {
					//TODO: Transparent panel showing new corner size roughly
				}
				else if(code == CANVAS_CODE) {
					clickBehaviour(code, x, y);
				}
			}
		};
		handPanel.setScrollBarHorizontal(false);
		handPanel.setScrollBarVertical(false);
		handPanel.addCanvas("canvas", 15, canvas, 1, HEADER_HEIGHT + 1, width, height, CANVAS_CODE, false);
		dCan = handPanel.getCanvas("canvas");
		int butWid = width * 9/10;
		int butHei = HEADER_HEIGHT * 9/10;
		handPanel.handleTextButton("texB", true, butWid / 2, butHei / 2, butWid, butHei, DrawingPage.DEFAULT_FONT, name, CODE_HEADER, Color.white, Color.black);
		updatePanel();
	}
	
	public void updatePanel() {
		handPanel.removeElementPrefixed("thck");
		int rA = canvas.getCanvasWidth() * canvas.getZoom();
		int tA = dCan.getY();
		int bA = dCan.getY() + canvas.getCanvasHeight() * canvas.getZoom();
		handPanel.addLine("line1", 5, true, rA, tA, rA, bA, 1, Color.black);
		handPanel.addLine("line2", 5, true, 0, bA, rA, bA, 1, Color.black);
		handPanel.handleThickRectangle("thck", true, 0, HEADER_HEIGHT, handPanel.getWidth(), handPanel.getHeight(), Color.black, 2);
		int size = 16;
		
		handPanel.handleImageButton("zoomIn", true, handPanel.getWidth() - size, HEADER_HEIGHT + size, size, size, "/assets/placeholder.png", CodeReference.CODE_INCREASE_ZOOM);
		handPanel.handleImageButton("zoomOut", true, handPanel.getWidth() - size, HEADER_HEIGHT + (int)(2.5 * size), size, size, "/assets/placeholder.png", CodeReference.CODE_DECREASE_ZOOM);
		
		handPanel.handleImageButton("imgB", true, handPanel.getWidth() - size, handPanel.getHeight() - size, size, size, "/assets/placeholder.png", CODE_RESIZE);
	}
	
	public void resizePanel(int wid, int hei) {
		wid = wid < MINIMUM_SIZE ? MINIMUM_SIZE : wid;
		hei = hei < MINIMUM_SIZE ? MINIMUM_SIZE : hei;
		handPanel.resize(wid, hei);
		dCan.updateElementSize(handPanel.getWidth(), handPanel.getHeight() - HEADER_HEIGHT);
		updatePanel();
	}

	public void move(int x, int y) {
		int oldX = handPanel.getPanelXLocation();
		int oldY = handPanel.getPanelYLocation();
		setLocation(oldX + x, oldY + y);
	}

	@Override
	public void updateCanvas(int x, int y, Color[][] cols) {
		openLock();
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i - x].length; j++) {
				if(cols[i -x][j - y] != null)
					canvas.setCanvasColor(i,  j,  cols[i - x][j - y]);
			}
		}
		closeLock();
	}

	public void updateCanvasMeta(Image[] imgs, int zoom) {
		if(imgs.length > 0)
			underlay = imgs[0];
		if(imgs.length > 1)
			overlay = imgs[1];
		setZoom(zoom);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setZoom(int in) {
		if(in <= 0) {
			in = 1;
		}
		if(canvas.getZoom() != in){
			openLock();
			canvas.setZoom(in);
			closeLock();
			updatePanel();
		}
	}

	@Override
	public void setLocation(int x, int y) {
		handPanel.setLocation(x, y);
	}

	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getPanelName() {
		return panelName;
	}
	
	public HandlePanel getPanel() {
		return handPanel;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public int getWidth() {
		return handPanel.getWidth();
	}

	@Override
	public int getHeight() {
		return handPanel.getHeight();
	}
	
}
