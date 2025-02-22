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
		Color grab = new Color(getColor(index), true);

		int[] cols = new int[] {grab.getRed() + chngR, grab.getGreen() + chngG, grab.getBlue() + chngB, grab.getAlpha() + chngA};
		for(int i = 0; i < cols.length; i++) {
			cols[i] = cols[i] < 0 ? 0 : cols[i] > 255 ? 255 : cols[i];
		}
		grab = new Color(cols[0], cols[1], cols[2], cols[3]);
		getCurrentPallet().setColor(index, grab.getRGB());
	}
	
	public void editColor(int index, Integer col) {
		getCurrentPallet().setColor(index, col);
	}

	public void addColor(Integer in) {
		getCurrentPallet().addColor(in);
	}
	
	public void removeColor(int in) {
		getCurrentPallet().removeColor(in);
		if(getCurrentPallet().getColors().size() == 0) {
			getCurrentPallet().addColor(Color.black.getRGB());
		}
	}
	
	public void addPallet(ArrayList<Integer> cols) {
		savedColors.add(new Pallet(cols));
	}
	
	public void addPallet() {
		savedColors.add(new Pallet());
	}
	
	public void removePallet(int ind) {
		savedColors.remove(ind);
		if(currPallet >= ind && currPallet != 0) {
			currPallet--;
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setCurrPallet(int index) {
		currPallet = fixIndex(index, savedColors.size());
	}
	
	public void setActiveColor(int index) {
		getCurrentPallet().setActiveColor(index);
	}

	
//---  Getter Methods   -----------------------------------------------------------------------

	public ArrayList<Integer> getColors(){
		return getCurrentPallet().getColors();
	}

	public ArrayList<Integer> getPallette(int index){
		return savedColors.get(index).getColors();
	}
	
	public Integer getColor(int index) {
		return getCurrentPallet().getColor(index);
	}
	
	public int getActiveColorIndex() {
		return getCurrentPallet().getActiveColorIndex();
	}

	public Integer getActiveColor() {
		return getCurrentPallet().getActiveColor();
	}
	
	public int getCurrentPalletIndex() {
		return currPallet;
	}
	
	public int getCurrentPalletCodeBase() {
		return currPallet * (PALLET_CODE_BUFFER + getCurrentPallet().getMaximumPalletSize());
	}
	
	public int getNumPallettes() {
		return savedColors.size();
	}
	
	public int getCurrentPalletSize() {
		return getCurrentPallet().getNumberColors();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private int fixIndex(int index, int max) {
		index = index < 0 ? 0 : index;
		index = index >= max ? max - 1 : index;
		return index;
	}

	private Pallet getCurrentPallet(){
		return savedColors.get(currPallet);
	}
	
}
