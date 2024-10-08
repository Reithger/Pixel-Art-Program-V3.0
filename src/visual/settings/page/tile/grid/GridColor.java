package visual.settings.page.tile.grid;

import java.awt.Color;

import visual.composite.HandlePanel;

public class GridColor implements GridIcon{

//---  Instance Variables   -------------------------------------------------------------------
	
	private Color col;
	private int code;
	private boolean selected;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public GridColor(Color in, int inCode) {
		col = in;
		code = inCode;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void draw(HandlePanel hP, String prefix, int posX, int posY, int size) {
		hP.handleButton(prefix + "_b_" + code, "move", 15, posX, posY,size, size, code);
		hP.handleRectangle(prefix + "_r_" + code, "move", 5, posX, posY, size, size, col, Color.black);
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			GridColor other = (GridColor)o;
			return getCode() == other.getCode() && getColor().equals(other.getColor());
		}
		catch(Exception e) {
			return false;
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public String getTooltipText() {
		String r = adjustLength(Integer.toHexString(col.getRed()));
		String g = adjustLength(Integer.toHexString(col.getGreen()));
		String b = adjustLength(Integer.toHexString(col.getBlue()));
		String a = adjustLength(Integer.toHexString(col.getAlpha()));
		return "#" + r + g + b + a;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
	protected Color getColor() {
		return col;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	@Override
	public void toggleSelected() {
		selected = !selected;
	}

	private String adjustLength(String in) {
		if(in.length() == 1) {
			return "0" + in;
		}
		return in;
	}
	
}
