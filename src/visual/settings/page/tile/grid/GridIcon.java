package visual.settings.page.tile.grid;

import visual.composite.HandlePanel;

public interface GridIcon {

	public abstract void draw(HandlePanel hP, String prefix, int posX, int posY, int size);
	
	public abstract boolean isSelected();
	
	public abstract void toggleSelected();
	
	public abstract int getCode();
	
}
