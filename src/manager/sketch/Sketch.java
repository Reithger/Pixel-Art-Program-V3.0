package manager.sketch;

import java.awt.Image;

import manager.curator.Curator;

public abstract class Sketch {
	
	protected static int counter;
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	private int activeLayer;
	private int zoom;
	private boolean drawable;
	private boolean update;
	
 	public Sketch(String nom, String ref) {
		name = nom;
		reference = ref;
		layerStart = 0;
		layerEnd = 0;
		activeLayer = 0;
		zoom = 1;
		drawable = false;
	}
	
	public abstract Sketch produceLayers(int layerSt, int layerEn);
	
	public abstract Image[] getUpdateImages(Curator c);
	
	public void releaseUpdate() {
		update = false;
	}
	
	public boolean needsUpdate() {
		boolean out = update;
		return out;
	}
	
	public boolean getDrawable() {
		return drawable;
	}
	
	public void setDrawable(boolean draw) {
		drawable = draw;
		update = true;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int in) {
		if(in <= 0) {
			in = 1;
		}
		zoom = in;
		update = true;
	}
	
	public int getActiveLayer() {
		return activeLayer;
	}
	
	public void setActiveLayer(int in) {
		activeLayer = in;
		update = true;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String in) {
		reference = in;
		update = true;
	}
	
	public String getName() {
		return name;
	}
	
	public void setLayerStart(int in) {
		layerStart = in;
		update = true;
	}
	
	public void setLayerEnd(int in) {
		layerEnd = in;
		update = true;
	}
	
	public int getLayerStart() {
		return layerStart;
	}
	
	public int getLayerEnd() {
		return layerEnd;
	}
	
}
