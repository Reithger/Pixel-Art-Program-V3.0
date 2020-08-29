package technical;

import java.awt.Color;

import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class Options{

	private ElementPanel options;
	
	public Options(int x, int y, int wid, int hei, WindowFrame ref) {
		options = new ElementPanel(x, y, wid, hei) {
			
		};
		ref.reservePanel("default", "options", options);
		drawOptionsPanel();
		options.setScrollBarVertical(false);
	}
	
	private void drawOptionsPanel() {
		int wid = options.getWidth();
		int hei = options.getHeight();
		options.addLine("lin", 5, false, 0, 0, wid, hei, 4, Color.black);
	}
	
	public ElementPanel getOptionsPanel() {
		return options;
	}
	
}
