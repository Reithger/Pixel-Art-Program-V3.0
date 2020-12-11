package visual.settings.page.tile.grid;

import java.awt.Color;

import visual.composite.HandlePanel;

public class GridImage implements GridIcon{

//---  Instance Variables   -------------------------------------------------------------------
	
	private int code;
	private String path;
	private boolean selected;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public GridImage(String inPath, int inCode) {
		code = inCode;
		path = inPath;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void draw(HandlePanel hP, String prefix, int posX, int posY, int size) {
		hP.handleRectangle(prefix + "_r_" + code, false, 5, posX, posY, size, size, Color.white, Color.black);
		hP.handleImageButton(prefix + "_b_" + code, false, posX, posY, size, size, path, code);
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			GridImage other = (GridImage)o;
			return getCode() == other.getCode() && getImagePath().equals(other.getImagePath());
		}
		catch(Exception e) {
			return false;
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	protected String getImagePath() {
		return path;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	@Override
	public void toggleSelected() {
		selected = !selected;
	}
}
