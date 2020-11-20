package manager.curator.picture;

public class LayerSeries {

	private int layerStart;
	private int layerEnd;
	
	public LayerSeries(int stIn, int enIn) {
		layerStart = stIn;
		layerEnd = enIn;
	}
	
	public int getLayerStart() {
		return layerStart;
	}
	
	public int getLayerEnd() {
		return layerEnd;
	}
	
	public boolean contains(int in) {
		return (in >= layerStart && in < layerEnd);
	}
	
	@Override
	public int hashCode() {
		return (layerStart + "_" + layerEnd).hashCode();
	}
	
}
