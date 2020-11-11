package visual.drawboard;

import visual.panel.Panel;

public interface Corkboard {
	
	public abstract Panel getPanel();
	
	public abstract void setLocation(int x, int y);
	
	public abstract void move(int x, int y);
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
