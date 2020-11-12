package manager;

public class SketchCanvas {
	
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	private int activeLayer;
	
	public SketchCanvas(String nom, String ref) {
		name = nom;
		reference = ref;
		layerStart = 0;
		layerEnd = 0;
		activeLayer = 0;
	}
	
	public SketchCanvas(String nom, String ref, int laySt, int layEn, int layAc) {
		name = nom;
		reference = ref;
		layerStart = laySt;
		layerEnd = layEn;
		activeLayer = layAc;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String in) {
		reference = in;
	}
	
	public String getName() {
		return name;
	}
	
	public int getActiveLayer() {
		return activeLayer;
	}
	
	public void setActiveLayer(int in) {
		activeLayer = in;
	}
	
	public void setLayerStart(int in) {
		layerStart = in;
	}
	
	public void setLayerEnd(int in) {
		layerEnd = in;
	}
	
	public int getLayerStart() {
		return layerStart;
	}
	
	public int getLayerEnd() {
		return layerEnd;
	}
	
}
