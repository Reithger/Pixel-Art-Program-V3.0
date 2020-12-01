package manager.curator.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;

import manager.curator.Component;

public class LayerPicture implements Component{

//---  Instance Variables   -------------------------------------------------------------------
	
	private int width;
	private int height;
	private ArrayList<ArtPicture> layers;	
	private HashMap<LayerSeries, ZoomCanvas> cache;
	private String savePath;
	private boolean changed;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public LayerPicture(String path) {
		File f = new File(path);
		changed = true;
		savePath = f.getParentFile().getAbsolutePath();
		cache = new HashMap<LayerSeries, ZoomCanvas>();
		if(f.isDirectory()) {
			//TODO: Get width, height from valid image and use if any images are broken to assign empty canvas
			//TODO: Or this is a manifest file/custom data type to decode? Probably want to error-proof this.
			//TODO: Like, just write the bytes for a header and the individual images, read them back out as metadata and files?
		}
		else {
			try {
				ArtPicture aP = new ArtPicture(f, 0);
				width = aP.getWidth();
				height = aP.getHeight();
				changed = true;
				layers = new ArrayList<ArtPicture>();
				layers.add(aP);
				cache = new HashMap<LayerSeries, ZoomCanvas>();
				generateImage();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failure during file reading to generate a single-layer LayerPicture");
			}
		}
	}
	
	public LayerPicture(int inWid, int inHei) {
		width = inWid;
		height = inHei;
		changed = true;
		layers = new ArrayList<ArtPicture>();
		cache = new HashMap<LayerSeries, ZoomCanvas>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void export(String path, String name, String typ, int scale, boolean composite) {
		File savePoint = new File(path + "/" + name + "." + typ);
		if(savePoint.exists()) {
			savePoint.delete();
		}
		try {
			ImageIO.write(generateImage(), typ, savePoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(composite && layers.size() > 1) {
			for(ArtPicture aP : layers) {
				aP.export(path, name + "_layer_" + aP.getLayer(), typ);
			}
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
	
	private void updateLayers() {
		boolean change = false;
		for(int i = 0; i < layers.size(); i++) {
			if(layers.get(i).getLayer() != i) {
				layers.get(i).setLayer(i);
				change = true;
			}
		}
		if(change)
			clearCache();
	}
	
	public BufferedImage generateImage() {
		return generateImageSetLayers(0, layers.size() - 1);
	}

	public BufferedImage generateImageSetLayers(int startLay, int endLay) {
		if(startLay < 0 || startLay > endLay || endLay > layers.size()) {
			System.out.println("Error: Illegal start or end indexes for generating an Image using a specific series of Layers");
			return null;
		}
		updateLayers();
		LayerSeries lS = new LayerSeries(startLay, endLay);
		if(cache.get(lS) != null) {
			return cache.get(lS).getImage();
		}
		ZoomCanvas zc = new ZoomCanvas(composeColorZoomCanvas(0, 0, width, height, startLay, endLay));
		cache.put(lS, zc);
		return zc.getImage();
	}
	
	private Color[][] composeColorZoomCanvas(int stX, int stY, int enX, int enY, int lS, int lE){
		if(lS < 0 || lE < 0) {
			return null;
		}
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
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	private void clearCache() {
		cache.clear();
		ensureDefaultImage();
	}
	
	private void ensureDefaultImage() {
		if(cache.get(new LayerSeries(0, layers.size() - 1)) == null) {
			Color[][] use = composeColorZoomCanvas(0, 0, width, height, 0, layers.size() - 1);
			if(use != null)
				cache.put(new LayerSeries(0, layers.size() - 1), new ZoomCanvas(use));
		}
	}
	
	public void designateUpdate() {
		changed = true;
	}

	public void resolvedUpdate() {
		changed = false;
	}

	public void setPixel(int x, int y, Color col, int layer) {
		layers.get(layer).setPixel(x, y, col);
		ensureDefaultImage();
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
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public boolean contains(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public int getNumLayers() {
		return layers.size();
	}
	
	public String getDefaultFilePath() {
		return savePath;
	}
	
	public boolean getUpdateStatus() {
		return changed;
	}

	public ArtPicture getLayer(int layer) {
		return layers.get(layer);
	}
	
	public ZoomCanvas getCanvas(int lS, int lE) {
		generateImageSetLayers(lS, lE);	//TODO: Make an 'ensureExists' method
		return cache.get(new LayerSeries(lS, lE));
	}
	
	public Color getColor(int x, int y, int layer) {
		return getLayer(layer).getColor(x, y);
	}
	
	public Color[][] getColorData(int layer){
		return getLayer(layer).getColorData();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addLayer() {
		addLayer(new ArtPicture(width, height, layers.size()));
		clearCache();
	}
	
	public void addLayer(ArtPicture in) {
		if(in.getWidth() != width || in.getHeight() != height) {
			System.out.println("Error: New ArtPicture object not of same size as composite Picture object");
			return;
		}
		layers.add(in);
		Collections.sort(layers);
		clearCache();
	}
	
	public void addLayer(ArtPicture in, int lH) {
		if(lH <= layers.size()) {
			layers.add(lH, in);
			updateLayers();
		}
		else {
			addLayer(in);
		}
		clearCache();
	}

//---  Remove Methods   -----------------------------------------------------------------------
	
	public void removeLayer(int ind) {
		layers.remove(ind);
		updateLayers();
		clearCache();
	}

}
