package manager.pen.color;

import java.awt.Color;
import java.util.ArrayList;

public class Pallet {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int MAXIMUM_SIZE = 30;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Integer> colors;
	private int active;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pallet() {
		colors = new ArrayList<Integer>();
		colors.add(Color.black.getRGB());
		colors.add(Color.white.getRGB());
		colors.add(new Color(255, 255, 255, 0).getRGB());
		colors.add(Color.blue.getRGB());
		colors.add(Color.yellow.getRGB());
		colors.add(Color.green.getRGB());
		colors.add(Color.red.getRGB());
		colors.add(Color.pink.getRGB());
		colors.add(Color.orange.getRGB());
		colors.add(Color.magenta.getRGB());
		active = 0;
	}
	
	public Pallet(ArrayList<Integer> cols) {
		colors = cols;
		active = 0;
		if(colors.size() == 0) {
			addColor(Color.black.getRGB());
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
	
	public void setColor(int index) {
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
	
	public int getActiveColor() {
		return active;
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
