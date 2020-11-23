package visual.settings.page.tile;

import java.awt.Font;

import visual.composite.HandlePanel;

public abstract class Tile {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static double SPACE_RATIO_VERTICAL = 4.0 / 5;
	protected final static Font SMALL_LABEL_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int height;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Tile() {
		
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void drawTile(int x, int y, HandlePanel p);
	
	public void assignMaximumVerticalSpace(int in) {
		height = (int)(in * SPACE_RATIO_VERTICAL);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract int getTileWidth();
	
	public int getHeight() {
		return height;
	}
	
}
