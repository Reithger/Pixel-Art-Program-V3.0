package visual.settings.page.tile;

import visual.composite.HandlePanel;

public abstract class Tile {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static double SPACE_RATIO_VERTICAL = 4.0 / 5;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	protected int height;
	
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
	
}
