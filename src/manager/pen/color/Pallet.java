package manager.pen.color;

import java.awt.Color;
import java.util.ArrayList;

public class Pallet {

//---  Constants   ----------------------------------------------------------------------------
	
	protected final static int MAXIMUM_SIZE = 30;
	public final static Color[] DEFAULT_COLORS = new Color[] {Color.black,
	                                              Color.white,
	                                              new Color(255, 255, 255, 0),
	                                              Color.blue,
	                                              Color.yellow,
	                                              Color.green,
	                                              Color.red,
	                                              Color.pink,
	                                              Color.orange,
	                                              Color.magenta};
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Integer> colors;
	private int active;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pallet() {
		colors = new ArrayList<Integer>();
		addDefaultColors();
		active = 0;
	}
	
	private void addDefaultColors() {
		for(Color c : DEFAULT_COLORS) {
			colors.add(c.getRGB());
		}
	}
	
	public Pallet(ArrayList<Integer> cols) {
		colors = cols;
		active = 0;
		if(colors.size() == 0) {
			addDefaultColors();
		}
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addColor(Integer in) {
		if(colors.size() < MAXIMUM_SIZE)
			colors.add(in);
	}
	
	public void removeColor(int index) {
		colors.remove(fixIndex(index));
		fixActive();
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setActiveColor(int index) {
		active = fixIndex(index);
	}
	
	public void setColor(int index, Integer col) {
		colors.set(fixIndex(index), col);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getMaximumPalletSize() {
		return MAXIMUM_SIZE;
	}
	
	public Integer getColor(int index) {
		return colors.get(fixIndex(index));
	}
	
	public ArrayList<Integer> getColors(){
		return colors;
	}
	
	public int getActiveColorIndex() {
		return active;
	}
	
	public Integer getActiveColor() {
		return colors.get(fixIndex(active));
	}
	
	public int getNumberColors() {
		return colors.size();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	private void fixActive() {
		active = fixIndex(active);
	}
	
	private int fixIndex(int index) {
		index = index < 0 ? 0 : index;
		index = index >= colors.size() ? colors.size() - 1 : index;
		return index;
	}
	
}
