package manager.pen.color;

import java.awt.Color;
import java.util.ArrayList;

public class Pallet {

	public final static int MAXIMUM_SIZE = 30;
	
	private ArrayList<Color> colors;
	private int active;
	
	public Pallet() {
		colors = new ArrayList<Color>();
		colors.add(Color.black);
		colors.add(Color.white);
		colors.add(new Color(255, 255, 255, 0));
		colors.add(Color.blue);
		colors.add(Color.yellow);
		colors.add(Color.green);
		colors.add(Color.red);
		colors.add(Color.pink);
		colors.add(Color.orange);
		colors.add(Color.magenta);
		active = 0;
	}
	
	public Pallet(ArrayList<Color> cols) {
		colors = cols;
		active = 0;
		if(colors.size() == 0) {
			addColor(Color.black);
		}
	}
	
	public void addColor(Color in) {
		if(colors.size() < MAXIMUM_SIZE)
			colors.add(in);
	}
	
	public void removeColor(int index) {
		index = fixIndex(index);
		colors.remove(index);
	}
	
	public void setColor(int index) {
		index = fixIndex(index);
		active = index;
	}
	
	public void setColor(int index, Color col) {
		index = fixIndex(index);
		colors.set(index, col);
	}
	
	public Color getColor(int index) {
		index = fixIndex(index);
		return colors.get(index);
	}
	
	public ArrayList<Color> getColors(){
		return colors;
	}
	
	public int getActiveColor() {
		return active;
	}
	
	private int fixIndex(int index) {
		index = index < 0 ? 0 : index;
		index = index >= colors.size() ? colors.size() - 1 : index;
		return index;
	}
	
}