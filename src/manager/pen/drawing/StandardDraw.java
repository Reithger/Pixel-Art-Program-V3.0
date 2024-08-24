package manager.pen.drawing;

import java.util.HashSet;

import manager.pen.changes.Change;
import manager.pen.drawtype.DrawType;
import manager.pen.drawtype.DrawTypeSelector;

public class StandardDraw {

//---  Instance Variables   -------------------------------------------------------------------

	private DrawType currMode;
	private int modeIndex;
	private int penSize;
	
	private boolean shade;
	private double blendQuotient;
	
	private int lastX;
	private int lastY;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public StandardDraw() {
		penSize = 2;
		updateCurrentDrawMode(DrawTypeSelector.PEN_DRAW_CIRCLE);
		modeIndex = 1;
		shade = false;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public Change[] draw(Integer[][] aP, int x, int y, int duration, Integer col) {
		x = x < 0 ? 0 : x;
		y = y < 0 ? 0 : y;
		x = x >= aP.length ? aP.length : x;
		y = y >= aP[0].length ? aP[0].length : y;
		if(duration == 0 || duration == -1) {
			lastX = x;
			lastY = y;
		}

		Change[] out = prepareChanges();
		currMode.tellAge(duration);
		drawSequence(x, y, col, out, aP);
		
		return out;
	}

	private void drawSequence(int x, int y, Integer col, Change[] out, Integer[][] ref) {
		Integer[][] apply = currMode.draw(col, penSize);
		
		HashSet<Point> points = new HashSet<Point>();
		Point a = new Point(x, y);
		Point b = new Point(lastX, lastY);
		
		points = LineCalculator.getPointsBetwixt(a, b);
		
		HashSet<Point> visited = new HashSet<Point>();
		
		for(Point p : points) {
			drawToPoint(ref, p.getX(), p.getY(), col, out, apply, visited);
		}

		lastX = x;
		lastY = y;
	}
	
	private void drawToPoint(Integer[][] aP, int x, int y, Integer col, Change[] out, Integer[][] apply, HashSet<Point> visited) {
		int wid = aP.length;
		int hei = aP[0].length;
		for(int i = 0; i < apply.length; i++) {
			for(int j = 0; j < apply[i].length; j++) {
				int actX = (x - apply.length / 2) + i;
				int actY = (y - apply[i].length / 2) + j;
				Point here = new Point(actX, actY);
				Integer newCol = apply[i][j];
				if(!visited.contains(here) && actX >= 0 && actY >= 0 && actX < wid && actY < hei && newCol != null){
					visited.add(here);
					int old = aP[actX][actY];
					newCol = shade ? blend(old, newCol) : newCol;
					out[0].addChange(actX, actY, old);
					out[1].addChange(actX, actY, newCol);
				}
			}
		}
	}
	
	/**
	 * Thank you to https://stackoverflow.com/a/29321264 for the algorithm on how to
	 * do correct color blending.
	 * 
	 * Function that takes two colors in Integer formats and blends them together in
	 * a style that gets proper light color gradients instead of the 'rush towards grey/brown'
	 * that a linear average would produce.
	 * 
	 * 
	 * @param curr
	 * @param newCol
	 * @return
	 */
	
	private int blend(Integer curr, Integer newCol) {
		double keep = (1.0 - blendQuotient);
		double bq = blendQuotient;
		int a = (int)(curr);
		int b = (int)(newCol);
		int broke = 255;
		int res = 0;
		for(int i = 0; i < 4; i++) {
			double colorOne = (a & broke) >> (8 * i);
			double colorTwo = (b & broke) >> (8 * i);
			double together = keep * Math.pow(colorOne, i == 3 ? 1 : 2) + bq * Math.pow(colorTwo, i == 3 ? 1 : 2);
			together = i == 3 ? together : Math.sqrt(together);
			res += (int)together << (8 * i);
			broke = broke << 8;
		} 
		return res;
	}

	private Change[] prepareChanges() {
		Change[] out = new Change[2];
		out[0] = new Change();	//undo
		out[0].setOverwrite(false);
		out[1] = new Change();	//redo
		return out;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setPenSize(int in) {
		in = in < 1 ? 1 : in;
		penSize = in;
	}
	
	public void setPenDrawType(int in) {
		modeIndex = in;
		updateCurrentDrawMode(in);
	}
	
	public void setBlendQuotient(double in) {
		if(in <= 1.0 && in >= 0.0)
			blendQuotient = in;
	}
	
	public void toggleShading() {
		shade = !shade;
	}
	
	private void updateCurrentDrawMode(int index) {
		currMode = DrawTypeSelector.getDrawType(index);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getPenSize() {
		return penSize;
	}
	
	public int getPenType() {
		return modeIndex;
	}
	
	public int[] getDrawTypes() {
		return DrawTypeSelector.getDrawTypes();
	}
	
	public double getBlendQuotient() {
		return blendQuotient;
	}
	
}
