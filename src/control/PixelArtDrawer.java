package control;

import manager.Manager;
import visual.drawboard.DrawingBoard;
import visual.frame.WindowFrame;
import visual.settings.SettingsBar;

/**
 * 
 * Key input is universal and redirected to one location (keyboard shortcuts not dependent on context (mostly))
 * 
 * @author Ada Clevinger
 *
 */

public class PixelArtDrawer {
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 7;
	
	private WindowFrame frame;
	
	private SettingsBar options;
	
	private DrawingBoard body;
	
	private Manager manager;
	
	public PixelArtDrawer() {
		makeModel();
		makeView();
	}
	
	private void makeModel() {
		manager = new Manager();
	}
	
	private void makeView() {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		options = new SettingsBar(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame);
		body = new DrawingBoard(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame);
		frame.reserveWindow(body.getWindowName());
		frame.showActiveWindow(body.getWindowName());
	}
	
}
