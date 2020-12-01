package manager.curator.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import misc.Canvas;

public class ZoomCanvas {

	private Canvas root;
	private HashMap<Integer, Canvas> derivatives;
	
	public ZoomCanvas(Color[][] cols) {
		root = new Canvas(cols);
		derivatives = new HashMap<Integer, Canvas>();
		derivatives.put(1, root);
	}
	
	public ZoomCanvas(Canvas inRot) {
		root = inRot;
		derivatives = new HashMap<Integer, Canvas>();
		derivatives.put(1, root);
	}
	
	public BufferedImage getImage() {
		return root.getImage();
	}
	
	public void setPixelColor(int x, int y, Color col) {
		root.setCanvasColor(x, y, col);
		for(Canvas c : derivatives.values()) {
			c.setCanvasColor(x, y, col);
		}
	}
	
	public Canvas getCanvas(int zoom) {
		if(derivatives.get(zoom) == null) {
			Canvas newC = new Canvas(root.getColorData());
			newC.setZoom(zoom);
			derivatives.put(zoom, newC);
		}
		return derivatives.get(zoom);
	}

}
