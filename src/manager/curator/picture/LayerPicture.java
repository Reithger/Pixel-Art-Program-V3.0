package manager.curator.picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;

import manager.curator.Component;

public class LayerPicture extends Component{

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<ArtPicture> layers;	
	private HashMap<LayerSeries, ZoomCanvas> cache;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public LayerPicture(String path) {
		File f = new File(path);
		designateUpdate();
		setDefaultFilePath(f.getParentFile().getAbsolutePath());
		cache = new HashMap<LayerSeries, ZoomCanvas>();
		if(f.isDirectory()) {
			//TODO: Get width, height from valid image and use if any images are broken to assign empty canvas
			//TODO: Or this is a manifest file/custom data type to decode? Probably want to error-proof this.
			//TODO: Like, just write the bytes for a header and the individual images, read them back out as metadata and files?
		}
		else {
			try {
				ArtPicture aP = new ArtPicture(f, 0);
				setWidth(aP.getWidth());
				setHeight(aP.getHeight());
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
		setWidth(inWid);
		setHeight(inHei);
		designateUpdate();
		layers = new ArrayList<ArtPicture>();
		cache = new HashMap<LayerSeries, ZoomCanvas>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void clearCache() {
		openLock();
		cache.clear();
		designateUpdate();
		closeLock();
	}
	
	//-- Meta Behaviour  --------------------------------------
	
	public void export(String path, String name, String typ, int scale, boolean composite) {
		File savePoint = new File(path + "/" + name + "." + typ);
		if(savePoint.exists()) {
			savePoint.delete();
		}
		try {
			ImageIO.write(generateImage(), typ, savePoint);
			setDefaultFilePath(path);
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

	//-- Image Generation  ------------------------------------
	
	public BufferedImage generateImage() {
		return generateImageSetLayers(0, layers.size() - 1);
	}

	public BufferedImage generateImageSetLayers(int startLay, int endLay) {
		LayerSeries lS = new LayerSeries(startLay, endLay);
		if(invalidLayerSeries(lS)) {
			return null;
		}
		return getCanvas(lS).getImage();
	}
	
	//-- Layer Manipulation  ----------------------------------
	
	public void moveLayers(int st, int en) {
		ArtPicture a = layers.get(st);
		layers.remove(st);
		if(st < en) {
			layers.add(en - 1, a);
		}
		else {
			layers.add(en, a);
		}
		updateLayers();
	}
	
	//TODO: Also provide needed zoom values to filter out unused ones
	
	public void optimizeStorage(HashSet<LayerSeries> layers) {
		openLock();
		for(LayerSeries lS : cache.keySet()) {
			if(!layers.contains(lS)) {
				cache.remove(lS);
				layers.remove(lS);
			}
		}
		for(LayerSeries lS : layers) {
			cache.put(lS, composeColorZoomCanvas(lS));
		}
		closeLock();
	}
	
	public void optimizeStorage(LayerSeries ref, HashSet<Integer> zooms) {
		getCanvas(ref).optimizeStorage(zooms);
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
	
	//-- Canvas Generation  -----------------------------------
	
	private ZoomCanvas composeColorZoomCanvas(LayerSeries lay) {
		return composeColorZoomCanvas(0, 0, getWidth(), getHeight(), lay);
	}

	private ZoomCanvas composeColorZoomCanvas(int stX, int stY, int enX, int enY, LayerSeries lay){
		if(invalidLayerSeries(lay)) {
			return null;
		}
		Color[][] out = new Color[enX - stX][enY - stY];
		for(int i = stX; i < enX; i++) {
			for(int j = stY; j < enY; j++) {
				out[i][j] = composeLayerColor(i, j, lay);
			}
		}
		return new ZoomCanvas(out);
	}
	
	private Color composeLayerColor(int x, int y, LayerSeries lay) {
		int lS = lay.getLayerStart();
		int lE = lay.getLayerEnd();
		Color c = layers.get(lS).getColor(x, y);
		for(int i = lS + 1; i <= lE; i++) {
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
			o = o == null ? new Color(255, 255, 255, 0) : o;
			int r = (int)(c.getRed() * alph) + (int)(o.getRed() * (1 - alph));
			int g = (int)(c.getGreen() * alph) + (int)(o.getGreen() * (1 - alph));
			int b = (int)(c.getBlue() * alph) + (int)(o.getBlue() * (1 - alph));
			int a = (int)(255 * (1.0 - (1.0 - alph) * (1.0 - (o.getAlpha() / 255.0))));
			return new Color(r, g, b, a);
		}
	}

//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addLayer() {
		addLayer(new ArtPicture(getWidth(), getHeight(), layers.size()));
	}
	
	public void addLayer(ArtPicture in) {
		if(in.getWidth() != getWidth() || in.getHeight() != getHeight()) {
			System.out.println("Error: New ArtPicture object not of same size as composite Picture object");
			return;
		}
		in.setLayer(layers.size());
		layers.add(in);
		updateLayers();
	}
	
	public void addLayer(ArtPicture in, int lH) {
		in.setLayer(lH);
		layers.add(lH, in);
		updateLayers();
	}

//---  Remove Methods   -----------------------------------------------------------------------
	
	public void removeLayer(int ind) {
		layers.remove(ind);
		updateLayers();
	}

//---  Setter Methods   -----------------------------------------------------------------------

	public void setPixel(int x, int y, Color col, int layer) {
		if(layers.get(layer) == null) {
			return;
		}
		layers.get(layer).setPixel(x, y, col);
		for(LayerSeries lS : cache.keySet()) {
			if(lS.contains(layer)) {
				Color col2 = composeLayerColor(x, y, lS);
				getCanvas(lS).setPixelColor(x, y, col2);
			}
		}
	}
	
	public void setRegion(int x, int y, Color[][] cols, int layer) {
		if(cols == null || layers.get(layer) == null) {
			return;
		}
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i - x].length; j++) {
				if(cols[i-x][j-y] != null)
					setPixel(i, j, cols[i - x][j - y], layer);
			}
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- Layer  -----------------------------------------------
	
	public Color[][] getColorData(int layer){
		return getLayer(layer).getColorData();
	}

	public ArtPicture getLayer(int layer) {
		return layers.get(layer);
	}
	
	public ZoomCanvas getCanvas(LayerSeries lS) {
		return getCanvas(lS.getLayerStart(), lS.getLayerEnd());
	}
	
	public ZoomCanvas getCanvas(int lS, int lE) {
		LayerSeries use = new LayerSeries(lS, lE);
		if(invalidLayerSeries(use)) {
			return null;
		}
		openLock();
		if(cache.get(use) == null) {
			cache.put(use, composeColorZoomCanvas(use));
		}
		ZoomCanvas out = cache.get(use);
		closeLock();
		return out;
	}
	
	public Color getColor(int x, int y, int layer) {
		return getLayer(layer).getColor(x, y);
	}
	
	private boolean invalidLayerSeries(LayerSeries lay) {
		if(lay.getLayerStart() < 0 || lay.getLayerEnd() >= layers.size() || lay.getLayerStart() > lay.getLayerEnd()) {
			return true;
		}
		return false;
	}
	
	//-- Meta Properties  -------------------------------------
	
	public int getNumLayers() {
		return layers.size();
	}
	
}
