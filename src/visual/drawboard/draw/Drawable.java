package visual.drawboard.draw;

import java.awt.Color;
import java.awt.Image;

import visual.drawboard.Corkboard;

public interface Drawable extends Corkboard {
	
	public abstract void generateCanvas(int width, int height);
	
	public abstract void updateCanvas(int x, int y, Color[][] cols);
	
	public abstract void updateCanvasMeta(Image[] imgs, int zoom);
	
}
