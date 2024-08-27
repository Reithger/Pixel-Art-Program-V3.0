package manager.curator;

public abstract class Component {

//---  Instance Variables   -------------------------------------------------------------------
	
	private int width;
	private int height;

	private String savePath;
	private boolean changed;

	private volatile boolean mutex;
	
//---  Operations   ---------------------------------------------------------------------------
	
	protected void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	protected void closeLock() {
		mutex = false;
	}

	public abstract void resize(int newWid, int newHei);
	
	public abstract void export(String filePath, String name, String fileType, int scale, boolean composite);
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	protected void setWidth(int in) {
		width = in;
	}
	
	protected void setHeight(int in) {
		height = in;
	}
	
	public void setDefaultFilePath(String in) {
		savePath = in;
	}

	public void designateUpdate() {
		changed = true;
	}
	
	public void resolveUpdate() {
		changed = false;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean getUpdateStatus() {
		return changed;
	}
	
	public boolean contains(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public String getDefaultFilePath() {
		return savePath;
	}

	public abstract int getNumLayers();
	
	
}
