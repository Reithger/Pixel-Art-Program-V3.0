package manager.pen;

import java.awt.Color;

import manager.curator.picture.ArtPicture;
import manager.curator.picture.LayerPicture;
import manager.pen.drawtype.DrawType;
import manager.pen.drawtype.DrawTypeSelector;

public class StandardDraw {

//---  Instance Variables   -------------------------------------------------------------------

	private int penSize;
	private DrawType currMode;
	private boolean shade;
	private double blendQuotient;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public StandardDraw() {
		penSize = 1;
		updateCurrentDrawMode(DrawTypeSelector.PEN_DRAW_CIRCLE);
		shade = false;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void draw(LayerPicture aP, int x, int y, int layer, Color col) {
		Color[][] apply = currMode.draw(col, penSize);
		int breadthX = apply.length / 2;
		int breadthY = apply[0].length / 2;
		int centerX = x - breadthX;
		int centerY = y - breadthY;
		for(int i = centerX - breadthX; i < centerX + breadthX; i++) {
			for(int j = centerY - breadthY; j < centerX + breadthY; j++) {
				Color newCol = apply[i - centerX + breadthX][j - centerY + breadthY];
				if(newCol != null) {
					if(shade) {
						newCol = blend(aP.getColor(i, j, layer), newCol);
					}
					aP.setPixel(i, j, newCol, layer);
				}
			}
		}
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
	
	public void incrementPenSize() {
		penSize++;
	}
	
	public void decrementPenSize() {
		penSize--;
	}
	
	public void setPenSize(int in) {
		penSize = in;
	}
	
	public void setPenDrawType(int in) {
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
	
	public double getBlendQuotient() {
		return blendQuotient;
	}
	
}
