package manager.pen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import manager.curator.picture.LayerPicture;
import manager.pen.changes.Change;
import manager.pen.changes.VersionHistory;
import manager.pen.color.ColorManager;
import manager.pen.drawing.DrawInstruction;
import manager.pen.drawing.Point;
import manager.pen.drawing.RegionDraw;
import manager.pen.drawing.StandardDraw;
import misc.Canvas;

public class Pen {
	
	/*
	 * 
	 * 
	 	 * Eraser?
	 	 * 
		 * 
		 * Mirroring image - just do it, flip canvas x/y
		 * Arbitrary color[][] to draw patterns - borrow from copy region activeColor[][] storage, need to gate the drawing for larger images
		 * System clipboard access to print - Toolkit probably, make new Image/canvas out of it
		 * 
	 */
	
	/*
	 * 
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
	
	public final static int REGION_MODE_OUTLINE = 0;
	public final static int REGION_MODE_FILL = 1;
	public final static int REGION_MODE_COPY = 2;
	public final static int REGION_MODE_PASTE = 3;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private VersionHistory changes;
	private ColorManager color;
	private StandardDraw pencil;
	private RegionDraw region;
	private volatile boolean mutex;
	
	private HashMap<String, Overlay> overlay;
	
	private int setMode;
	
	private HashMap<Integer, DrawInstruction> instructions;
	private int nextDuration;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		mutex = false;
		overlay = new HashMap<String, Overlay>();
		nextDuration = 0;
		instructions = new HashMap<Integer, DrawInstruction>();
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
	
	private void initializeCanvas(Canvas in) {
		for(int i = 0; i < in.getCanvasWidth(); i++) {
			for(int j = 0; j < in.getCanvasHeight(); j++) {
				in.setCanvasColor(i, j, new Color(255, 255, 255, 0));
			}
		}
	}

	//-- StandardDraw  ----------------------------------------
	
	public boolean draw(String nom, LayerPicture lP, int layer, int x, int y, int duration) {
		boolean force = false;
		if(overlay.get(nom) == null) {
			overlay.put(nom, new Overlay(lP.getWidth(), lP.getHeight()));
			initializeCanvas(overlay.get(nom).getCanvas());
			force = true;
		}
		
		if(duration == 0 || duration == -1) {
			instructions.clear();
			nextDuration = duration;
		}
		
		openLock();
		instructions.put(duration, new DrawInstruction(nom, getPenMode(), getRegionMode(), lP.getColorData(layer), x, y, getActiveColor(), layer));
		while(instructions.get(nextDuration) != null) {
			DrawInstruction dI = instructions.get(nextDuration);
			Change[] use = interpretInput(dI.getReference(), dI.getPenMode(), dI.getRegionMode(), dI.getColorArray(), dI.getColor(), dI.getX(), dI.getY(), nextDuration);
			if(use == null) {
				force = true;
			}
			commitChanges(lP, nom, layer, duration, use);
			instructions.remove(nextDuration);
			nextDuration++;
		}
		closeLock();
		
		return force;
	}
	
	private Change[] interpretInput(String nom, int penMode, int regionMode, Color[][] can, Color use, int x, int y, int duration) {
		boolean release = duration == -1;
		switch(penMode) {
			case PEN_MODE_DRAW:
				return pencil.draw(can, x, y, duration, use);
			case PEN_MODE_COLOR_PICK:
				Color nCol = can[x][y];
				color.addColor(nCol);
				color.setColor(30);
				setMode = PEN_MODE_DRAW;
				return null;
			case PEN_MODE_FILL:
				return fill(can, new Point(x, y), use);
			case PEN_MODE_REGION_SELECT:
				if(!region.hasActivePoint()) {
					region.resetPoints();
					region.assignPoint(new Point(x, y));
				}
				else if(release) {
					region.assignPoint(new Point(x, y));
					Change[] out = region.applyPointEffect(can, regionMode, use);
					region.resetPoints();
					overlay.get(nom).release(Overlay.REF_SELECT_BORDER);
					return out;
				}
				else {
					Change c = new Change();
					Point a = region.getFirstPoint();
					int x2 = a.getX();
					int y2 = a.getY();
					for(int i = x < x2 ? x : x2; i <= (x < x2 ? x2 : x) && i < can.length; i++) {
						for(int j = y < y2 ? y : y2; j <= (y < y2 ? y2 : y) && j < can[i].length; j++) {
							if((i == x || i == x2 || j == y || j == y2) && (Math.sqrt(Math.pow(x2 - i, 2) + Math.pow(y2 - j, 2)) < can.length / 20 || (Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2)) < can[0].length / 20)))
								c.addChange(i, j, inverse(can[i][j]));
						}
					}
					overlay.get(nom).instruct(Overlay.REF_SELECT_BORDER, c);
				}
				break;
			case PEN_MODE_REGION_APPLY:
				if(release || duration == 0) {
					overlay.get(nom).release(Overlay.REF_PASTE);
				}
				Change[] useC = region.applySavedRegion(can, regionMode, new Point(x, y));
				if(release) {
					return useC;
				}
				else {
					overlay.get(nom).instruct(Overlay.REF_PASTE, useC[1]);
				}
				break;
			default:
				break;
		}
		return new Change[] {new Change(), new Change()};
	}
	
	private Color inverse(Color in) {
		return new Color(255 - in.getRed(), 255 - in.getGreen(), 255 - in.getBlue(), 255);
	}
	
	private void commitChanges(LayerPicture lP, String ref, int layer, int duration, Change[] changesIn) {
		lP.setRegion(changesIn[1].getX(),changesIn[1].getY(), changesIn[1].getColors(), layer);
		changes.addChange(ref, layer, duration, changesIn[0], changesIn[1]);
	}
	
	private Change[] fill(Color[][] can, Point start, Color newCol) {
		LinkedList<Point> queue = new LinkedList<Point>();
		Change[] out = new Change[] {new Change(), new Change()};
		out[0].setOverwrite(false);
		queue.add(start);
		HashSet<Point> visited = new HashSet<Point>();
		Color oldCol = can[start.getX()][start.getY()];
		int wid = can.length;
		int hei = can[0].length;
		while(!queue.isEmpty()) {
			Point loc = queue.poll();
			int x = loc.getX();
			int y = loc.getY();
			if(visited.contains(loc) || x < 0 || y < 0 || x >= wid || y >= hei || !can[x][y].equals(oldCol)) {
				continue;
			}
			visited.add(loc);
			out[0].addChange(x, y, oldCol);
			out[1].addChange(x, y, newCol);
			for(int i = 0; i < 4; i++) {
				queue.add(new Point(loc.getX() + (1 * (i - 2 >= 0 ? i % 2 == 0 ? 1 : -1 : 0)), loc.getY() + (1 * (i < 2 ? i % 2 == 0 ? 1 : -1 : 0))));
			}
		}
		return out;
	}

	public void toggleShading() {
		pencil.toggleShading();
	}
	
	//-- Changes  ---------------------------------------------
	
	public void undo(String ref, LayerPicture lP) {
		Change c = changes.getUndo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), changes.getCurrentLayer(ref));
		}
	}
	
	public void redo(String ref, LayerPicture lP) {
		Change c = changes.getRedo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), changes.getCurrentLayer(ref));
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
	
	public Canvas getOverlay(String ref) {
		if(overlay.get(ref) == null) {
			return null;
		}
		return overlay.get(ref).getCanvas();
	}
	
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
