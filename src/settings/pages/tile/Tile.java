package settings.pages.tile;

import java.awt.Color;
import java.awt.Font;

import settings.pages.Page;

public abstract class Tile {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static double SPACE_RATIO_VERTICAL = 4.0 / 5;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Page reference;
	protected int height;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Tile() {
		
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void drawTile(int x, int y);
	
	public void assignPage(Page in) {
		reference = in;
		height = (int)(in.getHeight() * SPACE_RATIO_VERTICAL);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract int getTileWidth();
	
	protected Page getPage() {
		return reference;
	}
	
}
