package visual.drawboard.corkboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import misc.Canvas;
import visual.drawboard.DrawingPage;
import visual.panel.element.DrawnCanvas;

public class DrawPicture extends Corkboard{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CANVAS_CODE = -5839;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Canvas canvas;
	private DrawnCanvas dCan;
	
	private BufferedImage underlay;
	private BufferedImage overlay;

	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawPicture(String nom, String inPanel, int inWidth, int inHeight, DrawingPage ref) {
		super(nom, inPanel, inWidth, inHeight, ref);
		canvas = new Canvas(inWidth, inHeight) {
			@Override
			public void commandUnder(Graphics g) {
				drawUnder(g);
			}
			
			@Override
			public void commandOver(Graphics g) {
				drawOver(g);
			}
		};
		generatePanelLocal();
	}
	
	public DrawPicture(String nom, String inPanel, BufferedImage in, DrawingPage ref) {
		super(nom, inPanel, in.getWidth(null), in.getHeight(null), ref);
		canvas = new Canvas(in) {
			@Override
			public void commandUnder(Graphics g) {
				drawUnder(g);
			}
			
			@Override
			public void commandOver(Graphics g) {
				drawOver(g);
			}
		};
		generatePanelLocal();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	private void drawUnder(Graphics g) {
		//TODO: Integrate an image to draw over
	}
	
	private void drawOver(Graphics g) {
		//TODO: Integrate an overlay and grid
	}
	
	@Override
	protected void onClick(int code, int x, int y) {
		switch(code) {
			case CANVAS_CODE:
				int canX = x - dCan.getX();
				int canY = y - dCan.getY();
				if(canX >= 0 && canY >= 0){
					getReference().passOnDraw(canX, canY, getName());
				}
		}
	}

	@Override
	protected void onClickPress(int code, int x, int y) {
	}

	@Override
	protected void onClickRelease(int code, int x, int y) {
	}

	@Override
	protected void onDrag(int code, int x, int y) {
		if(code == CANVAS_CODE) {
			onClick(code, x, y);
		}
	}
	
	@Override
	public Corkboard duplicate(String newName, String inPanel) {
		DrawPicture out = new DrawPicture(newName, inPanel, canvas.getCanvasWidth(), canvas.getCanvasHeight(), getReference());
		out.updateCanvas(0, 0, canvas.getColorData());
		out.updateImages(new BufferedImage[] {underlay,  overlay}, canvas.getZoom());
		return out;
	}

	@Override
	public void updateImages(BufferedImage[] imgs, int zoom) {
		if(imgs.length > 0)
			underlay = imgs[0];
		if(imgs.length > 1)
			overlay = imgs[1];
		setZoom(zoom);
	}

	@Override
	protected void generatePanelLocal() {
		getPanel().addCanvas("canvas", 15, canvas, 1, HEADER_HEIGHT + 1, getWidth(), getHeight(), CANVAS_CODE, false);
		dCan = getPanel().getCanvas("canvas");
		updatePanel();
	}
	
	@Override
	public void updatePanelLocal() {
		int rA = canvas.getCanvasWidth() * canvas.getZoom();
		int tA = dCan.getY();
		int bA = dCan.getY() + canvas.getCanvasHeight() * canvas.getZoom();
		getPanel().addLine("line1", 5, true, rA, tA, rA, bA, 1, Color.black);
		getPanel().addLine("line2", 5, true, 0, bA, rA, bA, 1, Color.black);
		getPanel().handleThickRectangle("thck", true, 0, HEADER_HEIGHT, getPanel().getWidth(), getPanel().getHeight(), Color.black, 2);
	}

	@Override
	public void resizePanelLocal(int wid, int hei) {
		dCan.updateElementSize(getPanel().getWidth(), getPanel().getHeight() - HEADER_HEIGHT);
		updatePanel();
	}
	
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
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Canvas getCanvas() {
		return canvas;
	}

}
