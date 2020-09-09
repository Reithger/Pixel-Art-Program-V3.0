package meta;

import drawsurface.Body;
import technical.Options;
import technical.Pen;
import visual.frame.WindowFrame;

public class PixelArtWindow {
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 6;
	
	private WindowFrame frame;
	
	private Options options;
	
	private Body body;
	
	public static Pen pen;
	
	public PixelArtWindow() {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.reserveWindow("default");
		options = new Options(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame);
		body = new Body(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame);
	}
	
}
