package visual.settings.page.tile;

import java.awt.Font;

import visual.composite.HandlePanel;

public abstract class Tile implements Comparable<Tile>{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static double SPACE_RATIO_VERTICAL = 4.0 / 5;
	protected final static Font SMALL_LABEL_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int height;
	private int priority;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Tile() {
		
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void drawTile(int x, int y, HandlePanel p);
	
	public void assignMaximumVerticalSpace(int in) {
		height = (int)(in * SPACE_RATIO_VERTICAL);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPriority(int in) {
		priority = in;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract int getTileWidth();
	
	public int getHeight() {
		return height;
	}
	
	public int getPriority() {
		return priority;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int compareTo(Tile o) {
		int a = getPriority();
		int b = o.getPriority();
		if(a < b) {
			return -1;
		}
		else if(b < a) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
}
