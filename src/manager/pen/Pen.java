package manager.pen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import manager.curator.picture.LayerPicture;
import manager.pen.changes.Change;
import manager.pen.changes.VersionHistory;
import manager.pen.color.ColorManager;
import manager.pen.drawing.Point;
import manager.pen.drawing.RegionDraw;
import manager.pen.drawing.StandardDraw;

public class Pen {
	
	/*
	 * 
	 * 
	 	 * Eraser?
	 	 * 
		 * 
		 * Select mode - go into region select, after marking an area then pick contextual action vvv
			 * Copy region - boolean to initiate a region-select mode, integers for start position, activeColor[][] storage (multiple copied?)
			 * Outline region - ^^^, no activeColor[][] though just draw along edges
			 * Fill region - ^^^ but fill
		 *
		 * 
		 * Mirroring image - just do it, flip canvas x/y
		 * Arbitrary color[][] to draw patterns - borrow from copy region activeColor[][] storage, need to gate the drawing for larger images
		 * System clipboard access to print - Toolkit probably, make new Image/canvas out of it
		 * 
	 */
	
	/*
	 * 
	 * Select Region (multiple contextual actions after this)
	 * Superlayer marking
	 * Pattern drawing
	 * Shape drawing (arbitrary polygon)
	 * 
	 */
	
//---  Constants   ----------------------------------------------------------------------------
	
	public final static int PEN_MODE_DRAW = 0;
	public final static int PEN_MODE_MOVE_CANVAS = 1;
	public final static int PEN_MODE_COLOR_PICK = 2;
	public final static int PEN_MODE_FILL = 3;
	public final static int PEN_MODE_REGION_SELECT = 4;
	public final static int PEN_MODE_REGION_APPLY = 5;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private VersionHistory changes;
	private ColorManager color;
	private StandardDraw pencil;
	private RegionDraw region;
	private volatile boolean mutex;
	
	private int setMode;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		mutex = false;
		setMode = PEN_MODE_DRAW;
		changes = new VersionHistory();
		color = new ColorManager();
		pencil = new StandardDraw();
		region = new RegionDraw();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
	public void initializeCanvas(LayerPicture lP, int layer) {
		openLock();
		for(int i = 0; i < lP.getWidth(); i++) {
			for(int j = 0; j < lP.getHeight(); j++) {
				lP.setPixel(i, j, new Color(255, 255, 255, 0), layer);
			}
		}
		closeLock();
	}

	//-- StandardDraw  ----------------------------------------
	
	public boolean draw(String nom, LayerPicture lP, int layer, int x, int y, int duration) {
		boolean release = duration == -1;
		switch(setMode) {
			case PEN_MODE_DRAW:
				openLock();
				Change[] change = pencil.draw(lP, x, y, layer, duration, color.getActiveColor());
				changes.addChange(nom, layer, duration, change[0], change[1]);
				closeLock();
				return false;
			case PEN_MODE_COLOR_PICK:
				Color nCol = lP.getColor(x, y, layer);
				color.editColor(color.getActiveColorIndex(), nCol);
				setMode = PEN_MODE_DRAW;
				return true;
			case PEN_MODE_FILL:
				Color root = lP.getColor(x, y, layer);
				fill(lP, layer, new Point(x, y), root, color.getActiveColor());
				return false;
			case PEN_MODE_REGION_SELECT:
				if(duration == 0) {
					region.resetPoints();
					region.assignPoint(new Point(x, y));
				}
				else if(release) {
					region.assignPoint(new Point(x, y));
					region.applyPointEffect(lP, layer, color.getActiveColor());
				}
				return true;
			case PEN_MODE_REGION_APPLY:
				region.applySavedRegion(lP, layer, new Point(x, y));
				return true;
			default:
				return false;
		}
	}
	
	private void fill(LayerPicture lP, int layer, Point start, Color oldCol, Color newCol) {
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.add(start);
		HashSet<Point> visited = new HashSet<Point>();
		while(!queue.isEmpty()) {
			Point loc = queue.poll();
			int x = loc.getX();
			int y = loc.getY();
			if(visited.contains(loc) || !lP.contains(x, y) || !lP.getColor(x, y, layer).equals(oldCol)) {
				continue;
			}
			visited.add(loc);
			lP.setPixel(x, y, newCol, layer);
			for(int i = 0; i < 4; i++) {
				queue.add(new Point(loc.getX() + (1 * (i - 2 >= 0 ? i % 2 == 0 ? 1 : -1 : 0)), loc.getY() + (1 * (i < 2 ? i % 2 == 0 ? 1 : -1 : 0))));
			}
		}
	}

	public void toggleShading() {
		pencil.toggleShading();
	}
	
	//-- Changes  ---------------------------------------------
	
	public void undo(String ref, LayerPicture lP) {
		Change c = changes.getUndo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), c.getLayer());
		}
	}
	
	public void redo(String ref, LayerPicture lP) {
		Change c = changes.getRedo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), c.getLayer());
		}
	}
	
	public void disposeChanges() {
		changes = new VersionHistory();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void addColor(Color in) {
		color.addColor(in);
	}
	
	public void removeColor(int index) {
		color.removeColor(index);
	}
	
	public void editColor(int index, Color col) {
		color.editColor(index, col);
	}
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		color.editColor(index, chngR, chngG, chngB, chngA);
	}
	
	public void addPallet() {
		color.addPallet();
	}
	
	public void addPallet(ArrayList<Color> cols) {
		color.addPallet(cols);
	}
	
	public void removePallet(int index) {
		color.removePallet(index);
	}
	
	//-- RegionDraw  ------------------------------------------
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPenMode(int in) {
		setMode = in;
	}
	
	//-- StandardDraw  ----------------------------------------
	
	public void setPenSize(int in) {
		pencil.setPenSize(in);
	}
	
	public void incrementPenSize() {
		pencil.setPenSize(pencil.getPenSize() + 1);
	}
	
	public void decrementPenSize() {
		pencil.setPenSize(pencil.getPenSize() - 1);
	}
	
	public void setPenType(int index) {
		pencil.setPenDrawType(index);
	}
	
	public void setBlendQuotient(double in) {
		pencil.setBlendQuotient(in);
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void setActiveColor(int ind) {
		color.setColor(ind);
	}
	
	public void setActiveColor(Color in) {
		color.setColor(in);
	}

	public void setPallet(int in) {
		color.setPallet(in);
	}

	//-- RegionDraw  ------------------------------------------
	
	public void setRegionMode(int in) {
		region.setMode(in);
	}
	
	public void setRegionActiveSelect(int in) {
		region.setActiveSelect(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- StandardDraw  ----------------------------------------
	
	public int getPenSize() {
		return pencil.getPenSize();
	}
	
	public double getBlendQuotient() {
		return pencil.getBlendQuotient();
	}
	
	public int getPenType() {
		return pencil.getPenType();
	}
	
	public int[] getPenDrawTypes() {
		return pencil.getDrawTypes();
	}
	
	public int getPenMode() {
		return setMode;
	}
	
	//-- ColorManager  ----------------------------------------
	
	public Color getActiveColor() {
		return color.getActiveColor();
	}
	
	public int getActiveColorIndex() {
		return color.getActiveColorIndex();
	}
	
	public ArrayList<Color> getColors(){
		return color.getColors();
	}

	public int getCurrentPallet() {
		return color.getCurrentPalletIndex();
	}
	
	public int getCurrentPalletCodeBase() {
		return color.getCurrentPalletCodeBase();
	}
	
	//-- RegionDraw  ------------------------------------------
	
	public int getRegionMode() {
		return region.getMode();
	}
	
	public int getRegionActiveSelect() {
		return region.getActiveSelect();
	}
	
}
