package meta;

import drawsurface.DrawingBoard;
import settings.SettingsBar;
import settings.Pen;
import visual.frame.WindowFrame;

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
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 5;
	
	private WindowFrame frame;
	
	private SettingsBar options;
	
	private DrawingBoard body;
	
	public static Pen pen;
	
	public PixelArtDrawer() {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		options = new SettingsBar(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame);
		body = new DrawingBoard(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame);
		frame.reserveWindow(body.getWindowName());
		frame.showActiveWindow(body.getWindowName());
	}
	
}
