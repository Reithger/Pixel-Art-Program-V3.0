package manager.pen.drawing;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import manager.curator.picture.LayerPicture;
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
	private int nextDuration;
	
	private HashMap<Integer, DrawInstruction> instructions;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public StandardDraw() {
		penSize = 2;
		updateCurrentDrawMode(DrawTypeSelector.PEN_DRAW_CIRCLE);
		modeIndex = 1;
		shade = false;
		instructions = new HashMap<Integer, DrawInstruction>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public Change[] draw(LayerPicture aP, int x, int y, int layer, int duration, Color col) {
		x = x < 0 ? 0 : x;
		y = y < 0 ? 0 : y;
		x = x > aP.getWidth() ? aP.getWidth() : x;
		y = y > aP.getHeight() ? aP.getHeight() : y;
		if(duration == 0) {
			instructions.clear();
			lastX = x;
			lastY = y;
			nextDuration = 0;
		}
		instructions.put(duration, new DrawInstruction(x, y, col, layer));

		long tim = System.currentTimeMillis();
		System.out.println("Pre: " + tim + " " + Thread.currentThread());
		Change[] out = prepareChanges(layer);
		while(instructions.get(nextDuration) != null) {
			drawSequence(instructions.get(nextDuration), out, aP);
			instructions.remove(nextDuration);
			nextDuration++;
		}
		System.out.println("Post: " + (System.currentTimeMillis() - tim));
		
		return out;
	}

	private void drawSequence(DrawInstruction in, Change[] out, LayerPicture ref) {
		int layer = in.getLayer();
		Color col = in.getColor();
		int x = in.getX();
		int y = in.getY();
		Color[][] apply = currMode.draw(col, penSize);
		
		HashSet<Point> points = new HashSet<Point>();
		Point a = new Point(x, y);
		Point b = new Point(lastX, lastY);
		
		points = LineCalculator.getPointsBetwixt(a, b);


		long tim = System.currentTimeMillis();
		System.out.println("	Pre: ");
		
		for(Point p : points) {
			drawToPoint(ref, p.getX(), p.getY(), layer, col, out, apply);
		}
		

		System.out.println("	Post: " + (System.currentTimeMillis() - tim));
		
		lastX = x;
		lastY = y;
	}
	
	//TODO: Massive slowdown at some point here or in the mutex of Pen's draw function
	
	private void drawToPoint(LayerPicture aP, int x, int y, int layer, Color col, Change[] out, Color[][] apply) {
		long tim = System.currentTimeMillis();
		System.out.println("		Pre: ");
		for(int i = 0; i < apply.length; i++) {
			for(int j = 0; j < apply[i].length; j++) {
				Color newCol = apply[i][j];
				int actX = (x - apply.length / 2) + i;
				int actY = (y - apply[i].length / 2) + j;
				if(aP.contains(actX, actY) && newCol != null && !newCol.equals(aP.getColor(actX, actY, layer))) {
					Color old = aP.getColor(actX, actY, layer);
					newCol = shade ? blend(old, newCol) : newCol;
					out[0].addChange(actX, actY, old);
					out[1].addChange(actX, actY, newCol);
				}
			}
		}
		System.out.println("		Post: " + (System.currentTimeMillis() - tim));
		aP.setRegion(out[1].getX(), out[1].getY(), out[1].getColors(), layer);

		System.out.println("		Post Set: " + (System.currentTimeMillis() - tim));
	}
	
	private Color blend(Color curr, Color newCol) {
		double keep = (1.0 - blendQuotient);
		double bq = blendQuotient;
		int r = (int)(curr.getRed() * keep) + (int)(newCol.getRed() * bq);
		int g = (int)(curr.getGreen() * keep) + (int)(newCol.getGreen() * bq);
		int b = (int)(curr.getBlue() * keep) + (int)(newCol.getBlue() * bq);
		int a = (int)(curr.getAlpha() * keep) + (int)(newCol.getAlpha() * bq);
		return new Color(r, g, b, a);
	}

	private Change[] prepareChanges(int layer) {
		Change[] out = new Change[2];
		out[0] = new Change("", layer);	//undo
		out[0].setOverwrite(false);
		out[1] = new Change("", layer);	//redo
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
		if(in <= 1 && in >= 0)
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
