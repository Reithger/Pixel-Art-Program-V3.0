package visual.settings.page.tile.grid;

import java.awt.Color;
import java.util.ArrayList;

import control.CodeReference;

public class TileColorGrid extends TileGrid{

	public TileColorGrid(String label, int heigh) {
		super(label, heigh);
		updateGridIcons(new ArrayList<Color>(), 0);
	}
	
	private void updateGridIcons(ArrayList<Color> cols, int codeSt) {
		GridIcon[] icons = new GridIcon[cols.size() + 1];
		for(int i = 0; i < cols.size(); i++) {
			icons[i] = new GridColor(cols.get(i), codeSt + i, i == getActive());
		}
		icons[cols.size()] = new GridColor(Color.white, CodeReference.CODE_COLOR_ADD, false);
		assignGridIcons(icons);
	}
	
	public void assignColors(ArrayList<Color> inCol, int start) {
		updateGridIcons(inCol, start);
	}
	
}
