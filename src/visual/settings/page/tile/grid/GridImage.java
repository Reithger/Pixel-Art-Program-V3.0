package visual.settings.page.tile.grid;

import java.awt.Color;

import visual.composite.HandlePanel;

public class GridImage implements GridIcon{

	private int code;
	private String path;
	private boolean selected;
	
	public GridImage(String inPath, int inCode) {
		code = inCode;
		path = inPath;
	}
	
	@Override
	public void draw(HandlePanel hP, String prefix, int posX, int posY, int size) {
		hP.handleRectangle(prefix + "_r_" + code, false, 5, posX, posY, size, size, Color.white, Color.black);
		hP.handleImageButton(prefix + "_b_" + code, false, posX, posY, size, size, path, code);
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
	@Override
	public void toggleSelected() {
		selected = !selected;
	}
}
