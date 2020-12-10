package manager.pen.color;

import java.awt.Color;
import java.util.ArrayList;

public class ColorManager {
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int PALLET_CODE_BUFFER = 5;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int currPallet;
	private ArrayList<Pallet> savedColors;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ColorManager() {
		currPallet = 0;
		savedColors = new ArrayList<Pallet>();
		savedColors.add(new Pallet());
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
		getCurrentPallet().setColor(index, new Color(cols[0], cols[1], cols[2], cols[3]));
	}
	
	public void editColor(int index, Color col) {
		getCurrentPallet().setColor(index, col);
	}

	public void addColor(Color in) {
		getCurrentPallet().addColor(in);
	}
	
	public void removeColor(int in) {
		getCurrentPallet().removeColor(in);
		if(getActiveColorIndex() >= in) {
			setColor(getActiveColorIndex() - 1);
		}
		if(getCurrentPallet().getColors().size() == 0) {
			getCurrentPallet().addColor(Color.black);
		}
	}
	
	public void addPallet(ArrayList<Color> cols) {
		savedColors.add(new Pallet(cols));
	}
	
	public void addPallet() {
		savedColors.add(new Pallet());
	}
	
	public void removePallet(int ind) {
		savedColors.remove(ind);
		if(currPallet >= ind) {
			currPallet--;
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPallet(int index) {
		currPallet = fixIndex(index);
	}
	
	public void setColor(int index) {
		getCurrentPallet().setColor(fixIndex(index));
	}
	
	public void setColor(Color in) {
		addColor(in);
		getCurrentPallet().setColor(getCurrentPallet().getColors().size() - 1);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	public ArrayList<Color> getColors(){
		return getCurrentPallet().getColors();
	}
	
	public Color getColor(int index) {
		return getCurrentPallet().getColor(fixIndex(index));
	}
	
	public int getActiveColorIndex() {
		return getCurrentPallet().getActiveColor();
	}

	public Color getActiveColor() {
		return getColor(getCurrentPallet().getActiveColor());
	}
	
	public int getCurrentPalletIndex() {
		return currPallet;
	}
	
	public int getCurrentPalletCodeBase() {
		return currPallet * (PALLET_CODE_BUFFER + getCurrentPallet().getMaximumPalletSize());
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private int fixIndex(int index) {
		index = index < 0 ? 0 : index;
		index = index >= getCurrentPallet().getColors().size() ? getCurrentPallet().getColors().size() - 1 : index;
		return index;
	}

	private Pallet getCurrentPallet(){
		return savedColors.get(currPallet);
	}
	
}
