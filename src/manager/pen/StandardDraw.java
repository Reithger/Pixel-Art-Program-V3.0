package manager.pen;

import java.awt.Color;

import manager.curator.picture.LayerPicture;
import manager.pen.changes.Change;
import manager.pen.drawtype.DrawType;
import manager.pen.drawtype.DrawTypeSelector;

public class StandardDraw {

//---  Instance Variables   -------------------------------------------------------------------

	private int penSize;
	private DrawType currMode;
	private int modeIndex;
	private boolean shade;
	private double blendQuotient;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public StandardDraw() {
		penSize = 5;
		updateCurrentDrawMode(DrawTypeSelector.PEN_DRAW_CIRCLE);
		modeIndex = 1;
		shade = false;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public Change[] draw(LayerPicture aP, int x, int y, int layer, int duration, Color col) {
		Color[][] apply = currMode.draw(col, penSize);
		Change[] out = new Change[2];
		out[0] = new Change("", layer);	//undo
		out[1] = new Change("", layer);	//redo
		for(int i = 0; i < apply.length; i++) {
			for(int j = 0; j < apply[i].length; j++) {
				Color newCol = apply[i][j];
				int actX = (x - apply.length / 2) + i;
				int actY = (y - apply[i].length / 2) + j;
				if(aP.contains(actX, actY) && newCol != null) {
					Color old = aP.getColor(actX, actY, layer);
					newCol = shade ? blend(old, newCol) : newCol;
					out[0].addChange(actX, actY, old);
					aP.setPixel(actX, actY, newCol, layer);
					out[1].addChange(actX, actY, newCol);
				}
			}
		}
		return out;
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
	
	private void updateCurrentDrawMode(int index) {
		currMode = DrawTypeSelector.getDrawType(index);
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
