package meta;

import drawsurface.Body;
import technical.Options;
import visual.frame.WindowFrame;

public class PixelArtWindow {
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private WindowFrame frame;
	
	private Options options;
	
	private Body body;
	
	public PixelArtWindow() {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.reserveWindow("default");
		options = new Options(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 10, frame);
		body = new Body(0, SCREEN_HEIGHT/10, SCREEN_WIDTH, SCREEN_HEIGHT * 9 / 10, frame);
	}
	
}
