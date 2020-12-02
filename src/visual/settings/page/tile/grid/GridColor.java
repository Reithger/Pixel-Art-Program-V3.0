package visual.settings.page.tile.grid;

import java.awt.Color;

import visual.composite.HandlePanel;

public class GridColor implements GridIcon{

	private Color col;
	private int code;
	private boolean selected;
	
	public GridColor(Color in, int inCode, boolean active) {
		col = in;
		code = inCode;
		selected = active;
	}
	
	@Override
	public void draw(HandlePanel hP, String prefix, int posX, int posY, int index, int size) {
		hP.handleButton(prefix + "_b_" + code, false, posX, posY,size, size, code);
		hP.handleRectangle(prefix + "_r_" + code, false, 5, posX, posY, size, size, col, selected ? Color.yellow : Color.black);
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void toggleSelected() {
		selected = !selected;
	}

}
