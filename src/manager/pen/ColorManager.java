package manager.pen;

import java.awt.Color;
import java.util.ArrayList;

public class ColorManager {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int activeColor;
	private ArrayList<Color> savedColors;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ColorManager() {
		savedColors = new ArrayList<Color>();
		savedColors.add(Color.black);
		activeColor = 0;
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
	
	public void removeColor(int in) {
		savedColors.remove(in);
		if(activeColor >= in) {
			setColor(activeColor - 1);
		}
		if(savedColors.size() == 0) {
			savedColors.add(Color.black);
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setColor(int index) {
		if(index < 0) {
			index = 0;
		}
		activeColor = index;
	}
	
	public void setColor(Color in) {
		addColor(in);
		activeColor = savedColors.size() - 1;
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
	
	public int getActiveColorIndex() {
		return activeColor;
	}

	public Color getActiveColor() {
		return getColor(activeColor);
	}

}
