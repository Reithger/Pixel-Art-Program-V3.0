package visual.settings.page.tile.grid;

import visual.composite.HandlePanel;

public interface GridIcon {

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void draw(HandlePanel hP, String prefix, int posX, int posY, int size);
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract boolean isSelected();
	
	public abstract int getCode();
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public abstract void toggleSelected();

}
