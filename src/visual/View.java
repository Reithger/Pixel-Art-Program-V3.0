package visual;

import control.PixelArtDrawer;
import visual.drawboard.DrawingBoard;
import visual.frame.WindowFrame;
import visual.settings.SettingsBar;

public class View {
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 7;

	private PixelArtDrawer reference;
	
	private WindowFrame frame;
	
	private SettingsBar options;
	
	private DrawingBoard body;
	
	public View(PixelArtDrawer in) {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		options = new SettingsBar(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame, this);
		body = new DrawingBoard(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame, this);
		frame.reserveWindow(body.getWindowName());
		frame.showActiveWindow(body.getWindowName());
		reference = in;
	}

	public int requestIntInput(String text) {
		PopoutInputRequest piR = new PopoutInputRequest(text);
		
	}
	
	public void handOffInt(int code) {
		reference.interpretCode(code);
	}
	
	public void handOffClick(int x, int y, String nom) {
		reference.interpretDraw(x, y, nom);
	}

	
	
}
