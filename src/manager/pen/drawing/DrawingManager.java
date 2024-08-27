package manager.pen.drawing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import manager.curator.picture.LayerPicture;
import misc.Canvas;
import manager.pen.changes.Change;
import manager.pen.changes.DrawInstruction;
import manager.pen.changes.VersionHistory;

public class DrawingManager {
	
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

	public static int PEN_MODE_REGION_SELECT = -1;
	public static int PEN_MODE_REGION_APPLY = -2;

	
//---  Instance Variables   -------------------------------------------------------------------
	
	private boolean enabled;
	private StandardDraw pencil;
	private RegionDraw region;
	private VersionHistory changes;

	private HashMap<Integer, DrawInstruction> instructions;
	private int nextDuration;

	private HashMap<String, Overlay> overlay;

	private int setMode;
	private volatile boolean mutex;

	public static int PEN_MODE_DRAW;
	public static int PEN_MODE_MOVE_CANVAS;
	public static int PEN_MODE_COLOR_PICK;
	public static int PEN_MODE_FILL;
	
	public static int REGION_MODE_OUTLINE;
	public static int REGION_MODE_FILL;
	public static int REGION_MODE_COPY;
	public static int REGION_MODE_PASTE;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawingManager() {
		mutex = false;
		enabled = true;
		pencil = new StandardDraw();
		region = new RegionDraw();
		changes = new VersionHistory();
		nextDuration = 0;
		instructions = new HashMap<Integer, DrawInstruction>();
		overlay = new HashMap<String, Overlay>();
		setMode = PEN_MODE_DRAW;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignPenModeCodes(int draw, int moveCanvas, int colorPick, int fill) {
		PEN_MODE_DRAW = draw;
		PEN_MODE_MOVE_CANVAS = moveCanvas;
		PEN_MODE_COLOR_PICK = colorPick;
		PEN_MODE_FILL = fill;
	}
	
	public static void assignRegionModeCodes(int outline, int fill, int copy, int paste) {
		REGION_MODE_OUTLINE = outline;
		REGION_MODE_FILL = fill;
		REGION_MODE_COPY = copy;
		REGION_MODE_PASTE = paste;
		RegionDraw.assignRegionCodes(outline, fill, copy, paste);
	}
	
	//-- Setup  -----------------------------------------------

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
	
	//-- Meta Control  ----------------------------------------
	
	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
	//-- Input Interpretation/Drawing  ------------------------
	
	public boolean draw(String nom, LayerPicture lP, int layer, int x, int y, int duration, Color currColor) {
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
		instructions.put(duration, new DrawInstruction(nom, penMode, getRegionMode(), lP.getColorData(layer), x, y, currColor, layer));
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
		if(penMode == PEN_MODE_DRAW) {
			return interpretDraw(can, x, y, duration, use);
		}
		else if(penMode == PEN_MODE_FILL) {
			return interpretFill(can, x, y, duration, use);
		}
		else if(penMode == PEN_MODE_REGION_SELECT) {
			// Reinitializes the first corner of the area the user is selecting
			if(!region.hasActivePoint()) {
				region.resetPoints();
				region.assignPoint(new Point(x, y));
			}
			else {
				Change[] out = interpretRegionSelect(can, x, y, use, release, nom, regionMode);
				if(out != null) {
					return out;
				}
			}
		}
		else if(penMode == PEN_MODE_REGION_APPLY) {
				Change[] out = interpretRegionApply(can, x, y, duration, release, nom, regionMode);
				if(out != null) {
					return out;
				}
		}
		return new Change[] {new Change(), new Change()};
	}
	
	private Change[] interpretDraw(Integer[][] can, int x, int y, int duration, Integer use) {
		return pencil.draw(can, x, y, duration, use);
	}
	
	private Change[] interpretFill(Integer[][] can, int x, int y, int duration, Integer use) {
		return duration == 0 ? fill(can, new Point(x, y), use) : new Change[] {new Change(), new Change()};
	}
	
	private Change[] interpretRegionSelect(Integer[][] can, int x, int y, Integer use, boolean release, String nom, int regionMode) {
		// Once the user releases (continuous value range of input resets to -1), take the two points and apply
		// a result based on their positions and the other modes currently selected and remove the effect from
		// the overlay.
		if(release) {
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
			return null;
		}
	}

	private Change[] interpretRegionApply(Integer[][] can, int x, int y, int duration, boolean release, String nom, int regionMode) {
		if(release || duration == 0) {
			overlay.get(nom).release(Overlay.REF_PASTE);
		}
		Change[] useC = region.applySavedRegion(can, regionMode, new Point(x, y));
		if(release) {
			return useC;
		}
		else {
			overlay.get(nom).instruct(Overlay.REF_PASTE, useC[1]);
			return null;
		}
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
	
	private int inverse(Integer in) {
		return ~in.intValue() | 0xff000000;
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

	public void toggleShading() {
		pencil.toggleShading();
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
	
	//-- RegionDraw  ------------------------------------------
	
	public int getRegionMode() {
		return region.getMode();
	}
	
	public int getRegionActiveSelect() {
		return region.getActiveSelect();
	}
		
}
