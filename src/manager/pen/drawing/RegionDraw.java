package manager.pen.drawing;

import java.awt.Color;
import java.util.HashMap;

import manager.curator.picture.LayerPicture;
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
	
	public void applyPointEffect(LayerPicture lP, int layer, Color use) {
		int x1 = pointA.getX();
		int y1 = pointA.getY();
		int x2 = pointB.getX();
		int y2 = pointB.getY();
		if(x1 > x2) {
			int hold = x1;
			x1 = x2;
			x2 = hold;
		}
		if(y1 > y2) {
			int hold = y1;
			y1 = y2;
			y2 = hold;
		}
		switch(regionMode) {
			case REGION_MODE_OUTLINE:
				for(int i = x1; i <= x2; i++) {
					for(int j = y1; j <= y2; j++) {
						if(i == x1 || i == x2 || j == y1 || j == y2) {
							lP.setPixel(i, j, use, layer);
						}
					}
				}
				break;
			case REGION_MODE_FILL:
				for(int i = x1; i <= x2; i++) {
					for(int j = y1; j <= y2; j++) {
						lP.setPixel(i, j, use, layer);
					}
				}
				break;
			case REGION_MODE_COPY:
				copySelectedRegion(lP.getLayer(layer).getColorData(), x1, y1, x2, y2);
				break;
			default:
				break;
		}
	}
	
	public void applySavedRegion(LayerPicture lP, int layer, Point loc) {
		switch(regionMode) {
			case REGION_MODE_PASTE:
				Canvas c = saved.get(activeSelect);
				int x = loc.getX();
				int y = loc.getY();
				for(int i = 0; i < c.getCanvasWidth(); i++) {
					for(int j = 0; j < c.getCanvasHeight(); j++) {
						Color curCol = c.getCanvasColor(i, j);
						if(curCol != null && lP.contains(x  + i, j + y))
							lP.setPixel(x + i, j + y, curCol, layer);
					}
				}
				break;
			default:
				break;
		}
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
	
	public void copySelectedRegion(Color[][] in, int x1, int y1, int x2, int y2) {
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
