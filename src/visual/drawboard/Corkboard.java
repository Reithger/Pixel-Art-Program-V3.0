package visual.drawboard;

import visual.panel.Panel;

public interface Corkboard {

	public static final int HEADER_HEIGHT = 30;
	public static final int SIDEBAR_WIDTH = 30;
	
	public abstract Panel getPanel();
	
	public abstract void setLocation(int x, int y);
	
	public abstract void move(int x, int y);
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
