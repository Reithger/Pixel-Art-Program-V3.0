package visual.drawboard.display;

import visual.composite.HandlePanel;
import visual.panel.CanvasPanel;

public interface Display {

	public abstract HandlePanel generateDisplay(int x, int y);

}
