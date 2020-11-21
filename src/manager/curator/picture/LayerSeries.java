package manager.curator.picture;

public class LayerSeries implements Comparable<LayerSeries>{

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
		return (in >= layerStart && in <= layerEnd);
	}
	
	@Override
	public String toString() {
		return layerStart + "_" + layerEnd;
	}
	
	@Override
	public int hashCode() {
		return (toString()).hashCode();
	}
	
	@Override
	public boolean equals(Object in) {
		return(toString().equals(in.toString()));
	}
	
	@Override
	public int compareTo(LayerSeries in) {
		return(toString().compareTo(in.toString()));
	}
	
}
