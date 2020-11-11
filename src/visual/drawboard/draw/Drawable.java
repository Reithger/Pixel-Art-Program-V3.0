package visual.drawboard.draw;

import java.awt.Color;

import visual.drawboard.Corkboard;

public interface Drawable extends Corkboard {
	
	public abstract void generateCanvas(int width, int height);
	
	public abstract void updateCanvas(int x, int y, Color[][] cols);
	
}
