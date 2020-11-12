package manager;

public class SketchAnimation {
	
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	private int activeFrame;	//TODO: Controls for how it should be configured
	private int speed;
	
	public SketchAnimation(String nom) {
		name = nom;
	}
	
	public SketchAnimation(String nom, String ref, int laySt, int layEn) {
		name = nom;
		reference = ref;
		layerStart = laySt;
		layerEnd = layEn;
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
