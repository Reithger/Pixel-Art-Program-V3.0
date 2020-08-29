package drawsurface;

import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;

public interface Display {

	public abstract ElementPanel generateDisplay(int x, int y);
	
	public abstract CanvasPanel generateCanvas(int x, int y);
	
}
