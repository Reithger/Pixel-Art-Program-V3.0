package manager;

public class SketchPicture {
	
	private String reference;
	private String name;
	private int layerStart;
	private int layerEnd;
	
	public SketchPicture(String nom, String ref) {
		name = nom;
		reference = ref;
		layerStart = 0;
		layerEnd = 0;
	}
	
	public SketchPicture(String nom, String ref, int laySt, int layEn, int layAc) {
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
