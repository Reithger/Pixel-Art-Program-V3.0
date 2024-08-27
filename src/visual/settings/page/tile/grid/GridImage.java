package visual.settings.page.tile.grid;

import java.awt.Color;

import visual.composite.HandlePanel;

public class GridImage implements GridIcon{

//---  Instance Variables   -------------------------------------------------------------------
	
	private int code;
	private String path;
	private boolean selected;
	private String label;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public GridImage(String inPath, int inCode, String inLabel) {
		code = inCode;
		path = inPath;
		label = inLabel;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void draw(HandlePanel hP, String prefix, int posX, int posY, int size) {
		hP.handleRectangle(prefix + "_r_" + code, "move", 20, posX, posY, size, size, new Color(255, 255, 255, 0), Color.black);
		hP.handleRectangle(prefix + "_r2x	_" + code, "move", 10, posX, posY, size, size, Color.white, Color.white);
		hP.handleImageButton(prefix + "_b_" + code, "move", 15, posX, posY, size, size, path, code);
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
	public String getTooltipText() {
		return label;
	}
	
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
