package visual.settings.page.tile.grid;

import java.awt.Color;
import java.util.ArrayList;

import control.CodeReference;

public class TileColorGrid extends TileGrid{

	private ArrayList<Color> cols;
	private int active;
	private int codeSt;
	
	public TileColorGrid(String label, int heigh) {
		super(label, heigh);
		active = 0;
		codeSt = 0;
		cols = new ArrayList<Color>();
		updateGridIcons();
	}
	
	private void updateGridIcons() {
		GridIcon[] icons = new GridIcon[cols.size() + 1];
		for(int i = 0; i < cols.size(); i++) {
			icons[i] = new GridColor(cols.get(i), codeSt + i, i == active);
		}
		icons[cols.size()] = new GridColor(Color.white, CodeReference.CODE_COLOR_ADD, false);
		assignGridIcons(icons);
	}

	public void setActive(int in) {
		in = in < 0 ? 0 : in;
		in = in >= cols.size() ? cols.size() - 1 : in;
		getGridIcons()[active].toggleSelected();
		active = in;
		getGridIcons()[active].toggleSelected();
	}
	
	public void assignColors(ArrayList<Color> inCol, int start) {
		System.out.println(inCol);
		cols = inCol;
		codeSt = start;
		updateGridIcons();
	}
	
}
