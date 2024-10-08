package visual.settings.page.tile;

import java.awt.Font;
import java.util.ArrayList;

import visual.composite.HandlePanel;

/**
 * 
 * Abstract Tile object definition.
 * 
 * Note that a "Tile" indicates a sliced section of the sidescrolling menu bars
 * in each page of the art program, not the GridIcons that make up some types of
 * Tiles.
 * 
 * Tiles are responsible for displaying UI elements to the user and sending appropriate
 * code values to the Controller when the user prompts certain changes (clicks buttons).
 * 
 * Subtypes are TileBig (one large image and clickable button with a subheader), TileGrid
 * (many GridIcons or GridColors that can be interacted with by the user with a potential
 * display for the active selection), and TileNumericSelector (controls one variable with
 * a slider to display info to the user, such as current pen drawing size).
 * 
 * @author Reithger
 *
 */

public abstract class Tile implements Comparable<Tile>{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static double SPACE_RATIO_VERTICAL = 4.0 / 5;
	protected final static Font SMALL_LABEL_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int height;
	private int priority;
	private TileMetaInfo info;
	
	private int remX;
	private int remY;
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void drawTileMemory(HandlePanel p) {
		drawTile(remX, remY, p);
	}
	
	protected void logPosition(int x, int y) {
		remX = x;
		remY = y;
	}
	
	public abstract void drawTile(int x, int y, HandlePanel p);
	
	public abstract boolean dragTileProcess(int code, int x, int y);
	
	public void assignMaximumVerticalSpace(int in) {
		height = (int)(in * SPACE_RATIO_VERTICAL);
	}
	
	protected void drawLabel(String label, int x, int y, HandlePanel p) {
		p.handleText("gr_" + label, "move", 15, x + getTileWidth() / 2, y + getHeight() * 15 / 32, getTileWidth() * 2, getHeight() * 3 / 8, SMALL_LABEL_FONT, label);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPriority(int in) {
		priority = in;
	}
	
	public void setTileMetaInfo(String reference, Integer refresh, Integer push) {
		info = new TileMetaInfo(reference, refresh, push);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract String getTooltipText(int code);
	
	public abstract ArrayList<Integer> getAssociatedCodes();
	
	public abstract String getInfo();
	
	public String getReference() {
		return info.getReference();
	}
	
	public Integer getRefreshCode() {
		return info.getRefreshCode();
	}
	
	public Integer getPushChangeCode() {
		return info.getPushChangeCode();
	}
	
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
