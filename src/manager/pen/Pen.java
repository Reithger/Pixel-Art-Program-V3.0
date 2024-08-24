package manager.pen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import control.code.CodeReference;
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
	 * Split this up a bit for the different modes, messy at the moment
	 * 
	 */
	
//---  Constants   ----------------------------------------------------------------------------
	
	/**
	 * 
	 * Code values are set up here to correspond to external code input values so that
	 * when querying the state of the Model, there is an existing standard to understand
	 * the values by.
	 * 
	 * That is, if the current pen mode is Draw, that code is the same as the one the user
	 * triggers to change the mode to Draw so that we can know what image/overlay text corresponds
	 * to the backend model's state; thus we can tell the user with consistent visual language
	 * what the current mode is.
	 * 
	 * Region Select/Apply are outliers which have sub-options from the RegionDraw class, so
	 * they are local values that shouldn't overlap with any other code values we use which
	 * defer to the value stored in RegionDraw when contextually appropriate.
	 * 
	 */
	
	public final static int PEN_MODE_DRAW = CodeReference.CODE_PEN_MODE_DRAW;
	public final static int PEN_MODE_MOVE_CANVAS = CodeReference.CODE_PEN_MODE_MOVE_CANVAS;
	public final static int PEN_MODE_COLOR_PICK = CodeReference.CODE_PEN_MODE_COLOR_PICK;
	public final static int PEN_MODE_FILL = CodeReference.CODE_PEN_MODE_FILL;
	public final static int PEN_MODE_REGION_SELECT = -1;
	public final static int PEN_MODE_REGION_APPLY = -2;
	
	public final static int REGION_MODE_OUTLINE = RegionDraw.REGION_MODE_OUTLINE;
	public final static int REGION_MODE_FILL = RegionDraw.REGION_MODE_FILL;
	public final static int REGION_MODE_COPY = RegionDraw.REGION_MODE_COPY;
	public final static int REGION_MODE_PASTE = RegionDraw.REGION_MODE_PASTE;
	
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
	
	private boolean enabled;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		enabled = true;
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
		int root = new Color(255, 255, 255, 0).getRGB();
		for(int i = 0; i < lP.getWidth(); i++) {
			for(int j = 0; j < lP.getHeight(); j++) {
				lP.setPixel(i, j, root, layer);
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
		int penMode = getPenMode();
		if(!enabled) {
			return false;
		}
		if(overlay.get(nom) == null) {
			overlay.put(nom, new Overlay(lP.getWidth(), lP.getHeight()));
			initializeCanvas(overlay.get(nom).getCanvas());
			force = true;
		}
		
		if(duration == 0 && !instructions.isEmpty() && (penMode == PEN_MODE_REGION_SELECT || penMode == PEN_MODE_REGION_APPLY)) {
			DrawInstruction dI = instructions.get(new ArrayList<Integer>(instructions.keySet()).get(0));
			Change[] use = interpretInput(dI, -1);
			if(use == null) {
				force = true;
			}
			else {
				commitChanges(lP, nom, layer, duration, use);
			}
			nextDuration = 1;
			region.resetPoints();
			instructions.clear();
			return force;
		}
		
		if(duration == 0 || duration == -1) {
			if(duration == 0) {
				region.resetPoints();
			}
			instructions.clear();
			nextDuration = duration;
		}

		openLock();
		instructions.put(duration, new DrawInstruction(nom, penMode, getRegionMode(), lP.getColorData(layer), x, y, getActiveColor(), layer));
		while(instructions.get(nextDuration) != null) {
			DrawInstruction dI = instructions.get(nextDuration);
			Change[] use = interpretInput(dI, nextDuration);
			if(use == null) {
				force = true;
			}
			else {
				commitChanges(lP, nom, layer, duration, use);
			}
			instructions.remove(nextDuration - 1);
			nextDuration++;
		}
		closeLock();
		return force;
	}
	
	private Change[] interpretInput(DrawInstruction dI, int duration) {
		return interpretInput(dI.getReference(), dI.getPenMode(), dI.getRegionMode(), dI.getColorArray(), dI.getColor().getRGB(), dI.getX(), dI.getY(), duration);
	}
	
	private Change[] interpretInput(String nom, int penMode, int regionMode, Integer[][] can, Integer use, int x, int y, int duration) {
		boolean release = duration == -1;
		switch(penMode) {
			case PEN_MODE_DRAW:
				return pencil.draw(can, x, y, duration, use);
			case PEN_MODE_COLOR_PICK:
				Integer nCol = can[x][y];
				color.addColor(nCol);
				color.setColor(30);
				setMode = PEN_MODE_DRAW;
				return null;
			case PEN_MODE_FILL:
				return duration == 0 ? fill(can, new Point(x, y), use) : new Change[] {new Change(), new Change()};
			case PEN_MODE_REGION_SELECT:
				// Reinitializes the first corner of the area the user is selecting
				if(!region.hasActivePoint()) {
					region.resetPoints();
					region.assignPoint(new Point(x, y));
				}
				// Once the user releases (continuous value range of input resets to -1), take the two points and apply
				// a result based on their positions and the other modes currently selected and remove the effect from
				// the overlay.
				else if(release) {
					region.assignPoint(new Point(x, y));
					Change[] out = region.applyPointEffect(can, regionMode, use);
					region.resetPoints();
					overlay.get(nom).release(Overlay.REF_SELECT_BORDER);
					return out;
				}
				// Otherwise, tracks current position to know where to draw the two corners for visual aid in the selection being
				// performed, using the inversion of the underlying color for visibility.
				else {
					Change c = new Change();
					Point a = region.getFirstPoint();
					int x2 = a.getX();
					int y2 = a.getY();
					for(int i = x; (x < x2 ? i < x + can.length/20 : i > x - can.length/20) && i < can.length && i >= 0; i += (x < x2 ? 1 : -1)) {
						for(int j = y; (y < y2 ? j < y + can[0].length/20 : j > y - can[0].length/20) && j < can[0].length && j >= 0; j += (y < y2 ? 1 : -1)) {
							if((i == x || j == y )) {
								c.addChange(i, j, inverse(can[i][j]));
								int otX = x2 + (i - x) * -1;
								int otY = y2 + (j - y) * -1;
								otX = otX < 0 ? 0 : otX >= can.length ? can.length - 1 : otX;
								otY = otY < 0 ? 0 : otY >= can[0].length ? can[0].length - 1 : otY;
								c.addChange(otX, otY, inverse(can[otX][otY]));
							}
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
	
	private int inverse(Integer in) {
		return ~in.intValue() | 0xff000000;
	}
	
	private void commitChanges(LayerPicture lP, String ref, int layer, int duration, Change[] changesIn) {
		if(changesIn[1].getColors() == null) {
			return;
		}
		lP.setRegion(changesIn[1].getX(),changesIn[1].getY(), changesIn[1].getColors(), layer);
		changes.addChange(ref, layer, duration, changesIn[0], changesIn[1]);
	}
	
	private Change[] fill(Integer[][] can, Point start, int newCol) {
		LinkedList<Integer> queueX = new LinkedList<Integer>();
		LinkedList<Integer> queueY = new LinkedList<Integer>();
		Change[] out = new Change[] {new Change(), new Change()};
		out[0].setOverwrite(false);
		queueX.add(start.getX());
		queueY.add(start.getY());
		int oldCol = can[start.getX()][start.getY()];
		int wid = can.length;
		int hei = can[0].length;
		boolean[][] visited = new boolean[wid][hei];
		while(!queueX.isEmpty()) {
			int x = queueX.poll();
			int y = queueY.poll();
			if(visited[x][y] || !can[x][y].equals(oldCol)) {
				continue;
			}
			visited[x][y] = true;
			out[0].addChange(x, y, oldCol);
			out[1].addChange(x, y, newCol);
			for(int i = 0; i < 4; i++) {
				int a = x + (1 * (i - 2 >= 0 ? i % 2 == 0 ? 1 : -1 : 0));
				if(a < 0 || a >= wid) {
					continue;
				}
				int b = y + (1 * (i < 2 ? i % 2 == 0 ? 1 : -1 : 0));
				if(b < 0 || b >= hei){
					continue;
				}
				queueX.add(a);
				queueY.add(b);
			}
		}
		return out;
	}
	
	private String compileCoord(int x, int y) {
		return x + "," + y;
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
		color.addColor(in.getRGB());
	}
	
	public void removeColor(int index) {
		color.removeColor(index);
	}
	
	public void editColor(int index, Color col) {
		color.editColor(index, col.getRGB());
	}
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		color.editColor(index, chngR, chngG, chngB, chngA);
	}
	
	public void addPallet() {
		color.addPallet();
	}
	
	public void addPallet(ArrayList<String> cols) {
		ArrayList<Integer> co = new ArrayList<Integer>();
		for(String s : cols) {
			co.add(Integer.parseInt(s));
		}
		color.addPallet(co);
	}
	
	public void removePallet(int index) {
		color.removePallet(index);
	}
	
	//-- RegionDraw  ------------------------------------------
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPenMode(int in) {
		setMode = in;
		enable();
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
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
		color.setColor(in.getRGB());
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
	
	/**
	 * 
	 * This is relied on by DrawInstructions which needs to know that we are in a selection/application
	 * mode for a region on the pen, as well as by the Controller for updating the visuals.
	 * 
	 * @return
	 */
	
	public int getPenMode() {
		return setMode;
	}
	
	public int getPenModeCode() {
		return !enabled ? PEN_MODE_MOVE_CANVAS : setMode < 0 ? region.getMode() : setMode;
	}
	
	//-- ColorManager  ----------------------------------------
	
	public int getActiveColorValue() {
		return color.getActiveColor();
	}
	
	public Color getActiveColor() {
		return new Color(color.getActiveColor(), true);
	}
	
	public int getActiveColorIndex() {
		return color.getActiveColorIndex();
	}
	
	public ArrayList<Color> getColors(){
		ArrayList<Color> out = new ArrayList<Color>();
		for(Integer i : color.getColors()) {
			out.add(new Color(i, true));
		}
		return out;
	}
	
	public ArrayList<ArrayList<Integer>> getAllPallettes(){
		ArrayList<ArrayList<Integer>> out = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < color.getNumPallettes(); i++) {
			out.add(color.getPallette(i));
		}
		
		return out;
	}

	public int getCurrentPallet() {
		return color.getCurrentPalletIndex();
	}
	
	public int getCurrentPalletCodeBase() {
		return color.getCurrentPalletCodeBase();
	}
	
	public int getNumberPallettes() {
		return color.getNumPallettes();
	}
	
	//-- RegionDraw  ------------------------------------------
	
	public int getRegionMode() {
		return region.getMode();
	}
	
	public int getRegionActiveSelect() {
		return region.getActiveSelect();
	}
	
}
