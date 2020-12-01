package manager.sketch;

import manager.curator.Curator;
import misc.Canvas;

public abstract class Sketch {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	protected static int counter;
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	private int activeLayer;
	private int zoom;
	private boolean update;
	
//---  Constructors   -------------------------------------------------------------------------
	
 	public Sketch(String nom, String ref) {
		name = nom;
		reference = ref;
		layerStart = 0;
		layerEnd = 0;
		activeLayer = 0;
		zoom = 1;
	}
	
//---  Operations   ---------------------------------------------------------------------------
 	
	public abstract Sketch produceLayers(int layerSt, int layerEn);
	
 	public abstract Sketch copy();

//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract Canvas[] getUpdateImages(Curator c);
	
	public boolean needsUpdate() {
		return update;
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

	public void setName(String nom) {
		name = nom;
	}
	
	public void setZoom(int in) {
		if(in <= 0) {
			in = 1;
		}
		zoom = in;
	}

	public void setActiveLayer(int in) {
		activeLayer = in;
	}

	public void setReference(String in) {
		reference = in;
	}

	public void setLayerStart(int in) {
		layerStart = in;
	}
	
	public void setLayerEnd(int in) {
		layerEnd = in;
	}

}
