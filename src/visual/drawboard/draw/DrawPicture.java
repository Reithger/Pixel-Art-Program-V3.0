package visual.drawboard.draw;

import java.awt.Color;
import java.awt.Image;

import visual.drawboard.DrawingPage;
import visual.panel.CanvasPanel;

public class DrawPicture implements Drawable{

	private int zoom;
	private String name;
	private CanvasPanel canvas;
	private DrawingPage reference;
	
	public DrawPicture(String nom, int inWidth, int inHeight, int inZoom, DrawingPage ref) {
		zoom = inZoom;
		reference = ref;
		name = nom;
		generateCanvas(inWidth, inHeight);
	}
	
	@Override
	public void generateCanvas(int width, int height) {
		int wid = (int)(zoom * width);
		int hei = (int)(zoom * height);
		canvas = new CanvasPanel(0, 0, wid, hei, 1) {
			@Override
			public void clickEvent(int code, int x, int y) {
				if(code == -1)
					reference.passOnDraw(x, y, name);
				else
					reference.passOnCode(code, x, y, name);
			}
		};
	}

	public void move(int x, int y) {
		int oldX = canvas.getPanelXLocation();
		int oldY = canvas.getPanelYLocation();
		setLocation(oldX + x, oldY + y);
	}
	
	public CanvasPanel getPanel() {
		return canvas;
	}

	@Override
	public void updateCanvas(int x, int y, Color[][] cols) {
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i].length; j++) {
				canvas.setPixelColor(i,  j,  cols[i - x][j - y]);
			}
		}
	}

	@Override
	public void setLocation(int x, int y) {
		canvas.setLocation(x, y);
	}

	@Override
	public int getWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getHeight() {
		return canvas.getHeight();
	}
	
}
