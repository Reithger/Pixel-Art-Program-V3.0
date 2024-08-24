package visual.settings.page.tile.grid;

import visual.composite.HandlePanel;

/**
 * 
 * GridIcon is an interface to define the general behaviors of Grid objects; that is,
 * the things that are drawn inside of Tiles that provide the user interactability for
 * changing and knowing the current program state.
 * 
 * Most Grid objects have hovertext that describe their behavior or current state.
 * 
 * Subtypes are GridColor, which displays the current colors in the Pallete, GridImage,
 * which draws an image, and TileGrid
 * 
 * @author Reithger
 *
 */

public interface GridIcon {

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void draw(HandlePanel hP, String prefix, int posX, int posY, int size);
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract boolean isSelected();
	
	public abstract int getCode();
	
	public abstract String getTooltipText();
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public abstract void toggleSelected();

}
