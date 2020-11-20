package manager.curator.picture;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import manager.curator.Component;

public class LayerPicture implements Component{

	private int width;  //TODO Should store composite image and use sub-images to make recompilation of it after change faster
	private int height;
	private ArrayList<ArtPicture> layers;	
	private HashMap<LayerSeries, Canvas> cache;
	private boolean changed;
	
	public LayerPicture(String path) {
		File f = new File(path);
		changed = true;
		cache = new HashMap<LayerSeries, Canvas>();
		if(f.isDirectory()) {
			
		}
		else {
			
		}
	}
	
	public LayerPicture(int inWid, int inHei) {
		width = inWid;
		height = inHei;
		changed = true;
		layers = new ArrayList<ArtPicture>();
		cache = new HashMap<LayerSeries, Canvas>();
	}
	
	public boolean getUpdateStatus() {
		return changed;
	}
	
	public void designateUpdate() {
		changed = true;
		cache.clear();
	}
	
	public void resolvedUpdate() {
		changed = false;
	}
	
	public void export(String path, String typ, int scale, boolean composite) {
		//TODO: Export bufferedImage to path and, if true on composite, also save a folder of the layers
	}
	
	public void setPixel(int x, int y, Color col, int layer) {
		layers.get(layer).setPixel(x, y, col);
		for(LayerSeries lS : cache.keySet()) {
			if(lS.contains(layer)) {
				cache.get(lS).setPixelColor(x, y, composeLayerColor(x, y, lS.getLayerStart(), lS.getLayerEnd()));
			}
		}
	}
	
	public void setRegion(int x, int y, Color[][] cols, int layer) {
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < cols[i].length; j++) {
				setPixel(i, j, cols[i - x][j - y], layer);
			}
		}
	}
	
	public void addLayer() {
		addLayer(new ArtPicture(width, height, layers.size()));
	}
	
	public void addLayer(ArtPicture in) {
		if(in.getWidth() != width || in.getHeight() != height) {
			System.out.println("Error: New ArtPicture object not of same size as composite Picture object");
			return;
		}
		layers.add(in);
		Collections.sort(layers);
	}
	
	public void addLayer(ArtPicture in, int lH) {
		if(lH <= layers.size()) {
			layers.add(lH, in);
			updateLayers();
		}
		else {
			addLayer(in);
		}
	}

	public void moveLayers(int st, int en) {
		ArtPicture a = layers.get(st);
		layers.remove(st);
		if(st < en) {
			layers.add(en - 1, a);
		}
		else {
			layers.add(en, a);
		}
	}
	
	public ArtPicture getLayer(int layer) {
		return layers.get(layer);
	}
	
	public void removeLayer(int ind) {
		layers.remove(ind);
		updateLayers();
	}
	
	private void updateLayers() {
		boolean change = false;
		for(int i = 0; i < layers.size(); i++) {
			if(layers.get(i).getLayer() != i) {
				layers.get(i).setLayer(i);
				change = true;
			}
		}
		if(change)
			cache.clear();
	}
	
	public Image generateImage() {
		updateLayers();
		LayerSeries lS = new LayerSeries(0, layers.size());
		if(cache.get(lS) != null) {
			return cache.get(lS).getImage();
		}
		Canvas can = new Canvas(composeColorCanvas(0, 0, width, height, 0, layers.size()));
		cache.put(lS, can);
		return can.getImage();
	}

	public Image generateImageSetLayers(int startLay, int endLay) {
		if(startLay < 0 || startLay > endLay || endLay >= layers.size()) {
			System.out.println("Error: Illegal start or end indexes for generating an Image using a specific series of Layers");
			return null;
		}
		updateLayers();
		LayerSeries lS = new LayerSeries(startLay, endLay);
		if(cache.get(lS) != null) {
			return cache.get(lS).getImage();
		}
		Canvas can = new Canvas(composeColorCanvas(0, 0, width, height, startLay, endLay));
		cache.put(lS, can);
		return can.getImage();
	}
	
	private Color[][] composeColorCanvas(int stX, int stY, int enX, int enY, int lS, int lE){
		Color[][] out = new Color[enX - stX][enY - stY];
		for(int i = stX; i < enX; i++) {
			for(int j = stY; j < enY; j++) {
				out[i][j] = composeLayerColor(i, j, lS, lE);
			}
		}
		return out;
	}
	
	private Color composeLayerColor(int x, int y, int lS, int lE) {
		Color c = layers.get(lS).getColor(x, y);
		for(int i = lS + 1; i < lE; i++) {
			c = mergeLayerColor(layers.get(i).getColor(x, y), c);
		}
		return c;
	}
	
	private Color mergeLayerColor(Color top, Color bottom) {
		Color c = top;
		double alph = c.getAlpha() / 255.0;
		if(c.getAlpha() == 1.0) {
			return c;
		}
		else {
			Color o = bottom;
			o = o == null ? new Color(0, 0, 0) : o;
			return new Color((int)(c.getRed() * alph) + (int)(o.getRed() * (1 - alph)), (int)(c.getGreen() * alph) + (int)(o.getGreen() * (1 - alph)), (int)(c.getBlue() * alph) + (int)(o.getBlue() * (1 - alph)));
		}
	}
	
}
