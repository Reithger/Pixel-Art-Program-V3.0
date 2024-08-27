package manager.pen.drawing;

import java.util.HashMap;

import manager.pen.changes.Change;
import misc.Canvas;

public class RegionDraw {

//---  Constants   ----------------------------------------------------------------------------

	public static int REGION_MODE_OUTLINE;
	public static int REGION_MODE_FILL;
	public static int REGION_MODE_COPY;
	public static int REGION_MODE_PASTE;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Point pointA;
	private Point pointB;
	
	private HashMap<Integer, Canvas> saved;
	private int counter;
	
	private int activeSelect;
	
	private int regionMode;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public RegionDraw() {
		saved = new HashMap<Integer, Canvas>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignRegionCodes(int outline, int fill, int copy, int paste) {
		REGION_MODE_OUTLINE = outline;
		REGION_MODE_FILL = fill;
		REGION_MODE_COPY = copy;
		REGION_MODE_PASTE = paste;
	}
	
	public Change[] applyPointEffect(Integer[][] c, int inMode, Integer use) {
		Change[] out = new Change[] {new Change(), new Change()};
		if(pointA == null || pointB == null) {
			return out;
		}

		out[0].setOverwrite(false);
		if(regionMode == REGION_MODE_OUTLINE) {
			interpretRegionOutline(c, out, use);
		}
		else if(regionMode == REGION_MODE_FILL) {
			interpretRegionFill(c, out, use);
		}
		else if(regionMode == REGION_MODE_COPY) {
			copySelectedRegion(c);
		}
		return out;
	}
	
	private void interpretRegionFill(Integer[][] c, Change[] out, Integer use) {
		int x1 = pointA.getX();
		int y1 = pointA.getY();
		int x2 = pointB.getX();
		int y2 = pointB.getY();
		for(int i = smaller(x1, x2); i <= (larger(x1, x2)); i++) {
			for(int j = smaller(y1, y2); j <= larger(y1, y2); j++) {
				if(i >= 0 && i < c.length && j >= 0 && j < c[i].length) {
					out[0].addChange(i, j, c[i][j]);
					out[1].addChange(i, j, use);
				}
			}
		}
	}
	
	private void interpretRegionOutline(Integer[][] c, Change[] out, Integer use) {
		int x1 = pointA.getX();
		int y1 = pointA.getY();
		int x2 = pointB.getX();
		int y2 = pointB.getY();
		for(int i = smaller(x1, x2); i <= larger(x1, x2); i++) {
			for(int j = smaller(y1, y2); j <= larger(y1, y2); j++) {
				if((i == x1 || i == x2 || j == y1 || j == y2) && i >= 0 && i < c.length && j >= 0 && j < c[i].length) {
					out[0].addChange(i, j, c[i][j]);
					out[1].addChange(i, j, use);
				}
			}
		}
	}
	
	private int smaller(int a, int b) {
		return a < b ? a : b;
	}
	
	private int larger(int a, int b) {
		return a < b ? b : a;
	}
	
	public Change[] applySavedRegion(Integer[][] cIn, int inMode, Point loc) {
		Change[] out = new Change[] {new Change(), new Change()};
		out[0].setOverwrite(false);
		int wid = cIn.length;
		int hei = cIn[0].length;
		if(regionMode == REGION_MODE_PASTE) {
			Canvas c = saved.get(activeSelect);
			if(c == null) {
				return out;
			}
			return interpretRegionPaste(loc, c, wid, hei, out, cIn);
		}
		return out;
	}
	
	private Change[] interpretRegionPaste(Point loc, Canvas c, int wid, int hei, Change[] out, Integer[][] cIn) {
		int x = loc.getX();
		int y = loc.getY();
		for(int i = 0; i < c.getCanvasWidth(); i++) {
			for(int j = 0; j < c.getCanvasHeight(); j++) {
				Integer curCol = c.getCanvasIntValue(i, j);
				int usX = x + i;
				int usY = y + j;
				if(curCol != null && usX > 0 && usY > 0 && usX < wid && usY < hei)
					out[0].addChange(x + i, j + y, cIn[usX][usY]);
					out[1].addChange(x + i, y + j, curCol);
			}
		}
		return out;
	}
	
	public void assignPoint(Point in) {
		if(pointA == null) {
			pointA = in;
		}
		else {
			pointB = in;
		}
	}
	
	public void resetPoints() {
		pointA = null;
		pointB = null;
	}
	
	public void copySelectedRegion(Integer[][] in) {
		int x1 = pointA.getX();
		int y1 = pointA.getY();
		int x2 = pointB.getX();
		int y2 = pointB.getY();
		
		int wid = Math.abs(x2 - x1);
		int hei = Math.abs(y2 - y1);
		Canvas can = new Canvas(wid, hei);

		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				if(i < in.length && j < in[i].length && in[i][j] != null)
					can.setCanvasColor(i - x1, j - y1, in[i][j]);
			}
		}
		activeSelect = counter;
		saved.put(counter++, can);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setMode(int in) {
		regionMode = in;
	}
	
	public void setActiveSelect(int in) {
		activeSelect = in;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Point getFirstPoint() {
		return pointA;
	}
	
	public boolean hasActivePoint() {
		return pointA != null;
	}
	
	public int getActiveSelect() {
		return activeSelect;
	}
	
	public int getMode() {
		return regionMode;
	}
	
	public HashMap<Integer, Canvas> getSavedRegions(){
		return saved;
	}
	
}
