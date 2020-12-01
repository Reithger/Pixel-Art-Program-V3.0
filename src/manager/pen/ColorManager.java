package manager.pen;

import java.awt.Color;
import java.util.ArrayList;

public class ColorManager {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Color activeColor;
	private ArrayList<Color> savedColors;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ColorManager() {
		savedColors = new ArrayList<Color>();
		activeColor = Color.black;
		savedColors.add(activeColor);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		Color grab = getColor(index);
		if(grab == null) {
			return;
		}
		int[] cols = new int[] {grab.getRed() + chngR, grab.getGreen() + chngG, grab.getBlue() + chngB, grab.getAlpha() + chngA};
		for(int i = 0; i < cols.length; i++) {
			cols[i] %= 255;
			cols[i] = cols[i] < 0 ? 255 - cols[i] : cols[i];
		}
		savedColors.set(index, new Color(cols[0], cols[1], cols[2], cols[3]));
	}

	public void addColor(Color in) {
		savedColors.add(in);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setColor(int index) {
		activeColor = getColor(index);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public ArrayList<Color> getColors(){
		return savedColors;
	}
	
	public Color getColor(int index) {
		if(index >= 0 && index < savedColors.size()) {
			return savedColors.get(index);
		}
		return null;
	}

	public Color getActiveColor() {
		return activeColor;
	}

}
