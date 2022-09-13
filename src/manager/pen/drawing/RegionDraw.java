package manager.pen.drawing;

import java.util.HashMap;

import manager.pen.changes.Change;
import misc.Canvas;

public class RegionDraw {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int REGION_MODE_OUTLINE = 0;
	public final static int REGION_MODE_FILL = 1;
	public final static int REGION_MODE_COPY = 2;
	public final static int REGION_MODE_PASTE = 3;
	
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
	
	public Change[] applyPointEffect(Integer[][] c, int inMode, Integer use) {
		Change[] out = new Change[] {new Change(), new Change()};
		if(pointA == null || pointB == null) {
			return out;
		}
		int x1 = pointA.getX();
		int y1 = pointA.getY();
		int x2 = pointB.getX();
		int y2 = pointB.getY();

		out[0].setOverwrite(false);
		switch(inMode) {
			case REGION_MODE_OUTLINE:
				for(int i = x1 < x2 ? x1 : x2; i <= (x1 < x2 ? x2 : x1); i++) {
					for(int j = y1 < y2 ? y1 : y2; j <= (y1 < y2 ? y2 : y1); j++) {
						if((i == x1 || i == x2 || j == y1 || j == y2) && i >= 0 && i < c.length && j >= 0 && j < c[i].length) {
							out[0].addChange(i, j, c[i][j]);
							out[1].addChange(i, j, use);
						}
					}
				}
				break;
			case REGION_MODE_FILL:
				for(int i = x1 < x2 ? x1 : x2; i <= (x1 < x2 ? x2 : x1); i++) {
					for(int j = y1 < y2 ? y1 : y2; j <= (y1 < y2 ? y2 : y1); j++) {
						if(i >= 0 && i < c.length && j >= 0 && j < c[i].length) {
							out[0].addChange(i, j, c[i][j]);
							out[1].addChange(i, j, use);
						}
					}
				}
				break;
			case REGION_MODE_COPY:
				copySelectedRegion(c, x1, y1, x2, y2);
				break;
			default:
				break;
		}
		return out;
	}
	
	public Change[] applySavedRegion(Integer[][] cIn, int inMode, Point loc) {
		Change[] out = new Change[] {new Change(), new Change()};
		out[0].setOverwrite(false);
		int wid = cIn.length;
		int hei = cIn[0].length;
		switch(inMode) {
			case REGION_MODE_PASTE:
				Canvas c = saved.get(activeSelect);
				if(c == null) {
					return out;
				}
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
				break;
			default:
				break;
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
	
	public void copySelectedRegion(Integer[][] in, int x1, int y1, int x2, int y2) {
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
