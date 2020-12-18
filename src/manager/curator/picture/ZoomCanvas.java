package manager.curator.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import misc.Canvas;

public class ZoomCanvas {

//---  Instance Variables   -------------------------------------------------------------------
	
	private Canvas root;
	private HashMap<Integer, Canvas> derivatives;
	
//---  Constructors   -------------------------------------------------------------------------
	
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
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void optimizeStorage(HashSet<Integer> sizes) {
		for(int i : derivatives.keySet()) {
			if(!sizes.contains(i)) {
				derivatives.remove(i);
				sizes.remove(i);
			}
		}
		for(int i : sizes) {
			derivatives.put(i, makeZoomedCanvas(i));
		}
	}
	
	private Canvas makeZoomedCanvas(int zoom) {
		Canvas newC = new Canvas(root.getColorData());
		newC.setZoom(zoom);
		return newC;
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPixelColor(int x, int y, Color col) {
		root.setCanvasColor(x, y, col);
		for(Canvas c : derivatives.values()) {
			c.setCanvasColor(x, y, col);
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public BufferedImage getImage() {
		return root.getImage();
	}

	public Canvas getRoot() {
		return root;
	}
	
	public Canvas getCanvas(int zoom) {
		if(derivatives.get(zoom) == null) {
			derivatives.put(zoom, makeZoomedCanvas(zoom));
		}
		return derivatives.get(zoom);
	}

}
