package visual.drawboard.draw;

import java.awt.Graphics;

import visual.panel.CanvasPanel;

public class DrawPicture implements Drawable{

	private double scale;
	private int width;
	private int height;
	
	public DrawPicture(double inScale, int inWidth, int inHeight) {
		scale = inScale;
		width = inWidth;
		height = inHeight;
	}
	
	@Override
	public CanvasPanel generateCanvas(int x, int y) {
		int wid = (int)(scale * width);
		int hei = (int)(scale * height);
		CanvasPanel c = new CanvasPanel(x, y, wid, hei) {
			@Override
			public void keyEvent(char event) {
				//TODO: Keyboard shortcuts for Picture (change draw layer)
			}
			
			@Override
			public void commandUnder(Graphics g) {
				g.drawImage(generateImageSetLayers(0, activeLayer - 1), 0, 0, null);
			}
			
		};
		c.updateCanvas(layers.get(activeLayer).getColorData());
		return c;
	}
	
}
