package manager.curator;

public interface Component {

	public abstract void export(String filePath, String name, String fileType, int scale, boolean composite);
	
	public abstract String getDefaultFilePath();
	
	public abstract int getNumLayers();
	
}
