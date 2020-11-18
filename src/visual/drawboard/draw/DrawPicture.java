package visual.drawboard.draw;

import java.awt.Color;

import visual.composite.HandlePanel;
import visual.drawboard.DrawingPage;
import visual.panel.element.DrawnCanvas;

public class DrawPicture implements Drawable{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int MINIMUM_SIZE = 150;
	private final static int CANVAS_CODE = 55;
	private final static int CODE_RESIZE = 54;
	private final static int CODE_HEADER = 53;
	private final static int CODE_ZOOM_IN = 52;
	private final static int CODE_ZOOM_OUT = 51;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int zoom;
	private String name;
	private HandlePanel handPanel;
	private DrawnCanvas canvas;
	private DrawingPage reference;
	
	private boolean draggingHeader;
	private boolean draggingResize;
	
	private int lastX;
	
	private int lastY;
	
	private boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawPicture(String nom, int inWidth, int inHeight, DrawingPage ref) {
		zoom = 1;
		reference = ref;
		mutex = false;
		name = nom;
		int wid = inWidth < MINIMUM_SIZE ? MINIMUM_SIZE : inWidth;
		int hei = inHeight < MINIMUM_SIZE ? MINIMUM_SIZE : inHeight;
		canvas = new DrawnCanvas(1, HEADER_HEIGHT + 1, wid, hei, CANVAS_CODE, inWidth, inHeight);
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
						int canX = x - canvas.getX();
						int canY = y - canvas.getY();
						if(canX >= 0 && canY >= 0){
							reference.passOnDraw(canX / zoom, canY / zoom, name);
						}
						break;
					case CODE_ZOOM_IN:
						System.out.println("H");
						setZoom(getZoom() + 1);
						break;
					case CODE_ZOOM_OUT:
						System.out.println("G");
						setZoom(getZoom() - 1);
						break;
					case CODE_HEADER:
						break;
					case CODE_RESIZE:
						break;
					default:
						if(code != -1)
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
					handPanel.resize(handPanel.getWidth() + (x - lastX), handPanel.getHeight() + (y - lastY));
					canvas.updateElementSize(handPanel.getWidth(), handPanel.getHeight() - HEADER_HEIGHT);
					updatePanel();
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
				if(code == CANVAS_CODE) {
					clickBehaviour(code, x, y);
				}
			}
		};
		handPanel.setScrollBarHorizontal(false);
		handPanel.setScrollBarVertical(false);
		handPanel.addCanvas("canvas", canvas, true);
		int butWid = width * 9/10;
		int butHei = HEADER_HEIGHT * 9/10;
		handPanel.handleTextButton("texB", true, butWid / 2, butHei / 2, butWid, butHei, DrawingPage.DEFAULT_FONT, name, CODE_HEADER, Color.white, Color.black);
		updatePanel();
	}
	
	public void updatePanel() {
		handPanel.removeElementPrefixed("thck");
		int rA = canvas.getCanvasWidth() * zoom;
		int tA = canvas.getY();
		int bA = canvas.getY() + canvas.getCanvasHeight() * zoom;
		handPanel.addLine("line1", 5, true, rA, tA, rA, bA, 1, Color.black);
		handPanel.addLine("line2", 5, true, 0, bA, rA, bA, 1, Color.black);
		handPanel.handleThickRectangle("thck", true, 0, HEADER_HEIGHT, handPanel.getWidth(), handPanel.getHeight(), Color.black, 2);
		int size = 16;
		
		handPanel.handleImageButton("zoomIn", true, handPanel.getWidth() - size, HEADER_HEIGHT + size, size, size, "/assets/placeholder.png", CODE_ZOOM_IN);
		handPanel.handleImageButton("zoomOut", true, handPanel.getWidth() - size, HEADER_HEIGHT + (int)(2.5 * size), size, size, "/assets/placeholder.png", CODE_ZOOM_OUT);
		
		handPanel.handleImageButton("imgB", true, handPanel.getWidth() - size, handPanel.getHeight() - size, size, size, "/assets/placeholder.png", CODE_RESIZE);
	}

	public void move(int x, int y) {
		int oldX = handPanel.getPanelXLocation();
		int oldY = handPanel.getPanelYLocation();
		setLocation(oldX + x, oldY + y);
	}

	@Override
	public void updateCanvas(int x, int y, Color[][] cols) {
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i - x].length; j++) {
				if(cols[i -x][j - y] != null)
					canvas.setCanvasColor(i,  j,  cols[i - x][j - y]);
			}
		}
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setZoom(int in) {
		System.out.println("In: " + in);
		openLock();
		if(in <= 0) {
			in = 1;
		}
		zoom = in;
		canvas.setZoom(zoom);
		updatePanel();
		closeLock();
		System.out.println("Out: " + in);
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
	
	public int getZoom() {
		return zoom;
	}

	public HandlePanel getPanel() {
		return handPanel;
	}
	
	public DrawnCanvas getCanvas() {
		return canvas;
	}

	@Override
	public int getWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getHeight() {
		return canvas.getHeight();
	}
	
}
