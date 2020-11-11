package visual.drawboard.display;

import java.awt.Image;

import visual.drawboard.Corkboard;

public interface Display extends Corkboard {

	public abstract void generateDisplay(int width, int height);

	public abstract void updateDisplay(Image ... image);

}
