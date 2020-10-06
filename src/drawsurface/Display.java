package drawsurface;

import meta.HandlePanel;
import visual.panel.CanvasPanel;

public interface Display {

	public abstract HandlePanel generateDisplay(int x, int y);
	
	public abstract CanvasPanel generateCanvas(int x, int y);
	
}
