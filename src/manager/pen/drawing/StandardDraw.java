package manager.pen.drawing;

import java.awt.Color;
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
	
	private int blend(Integer curr, Integer newCol) {
		double keep = (1.0 - blendQuotient);
		double bq = blendQuotient;
		return (int)((curr * keep) + (bq * newCol));
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
