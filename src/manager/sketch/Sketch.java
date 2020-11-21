package manager.sketch;

import java.awt.image.BufferedImage;

import manager.curator.Curator;

public abstract class Sketch {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	protected static int counter;
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	private int activeLayer;
	private int zoom;
	private boolean drawable;
	private boolean update;
	
//---  Constructors   -------------------------------------------------------------------------
	
 	public Sketch(String nom, String ref) {
		name = nom;
		reference = ref;
		layerStart = 0;
		layerEnd = 0;
		activeLayer = 0;
		zoom = 1;
		drawable = false;
	}
	
//---  Operations   ---------------------------------------------------------------------------
 	
	public abstract Sketch produceLayers(int layerSt, int layerEn);
	
 	public abstract Sketch copy();

//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract BufferedImage[] getUpdateImages(Curator c);
	
	public boolean needsUpdate() {
		return update;
	}
	
	public boolean getDrawable() {
		return drawable;
	}
	
	public int getLayerStart() {
		return layerStart;
	}
	
	public int getLayerEnd() {
		return layerEnd;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReference() {
		return reference;
	}
	
	public int getActiveLayer() {
		return activeLayer;
	}
	
	public int getZoom() {
		return zoom;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void flagUpdate() {
		update = true;
	}
	
	public void releaseUpdate() {
		update = false;
	}

	public void setDrawable(boolean draw) {
		drawable = draw;
		update = true;
	}

	public void setName(String nom) {
		name = nom;
	}
	
	public void setZoom(int in) {
		if(in <= 0) {
			in = 1;
		}
		zoom = in;
		update = true;
	}

	public void setActiveLayer(int in) {
		activeLayer = in;
		update = true;
	}

	public void setReference(String in) {
		reference = in;
		update = true;
	}

	public void setLayerStart(int in) {
		layerStart = in;
		update = true;
	}
	
	public void setLayerEnd(int in) {
		layerEnd = in;
		update = true;
	}

}
