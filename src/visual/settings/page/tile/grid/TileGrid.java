package visual.settings.page.tile.grid;

import java.util.ArrayList;

import visual.composite.HandlePanel;
import visual.settings.page.tile.Tile;

public class TileGrid extends Tile{

	private GridIcon[] icons;
	private String label;
	private int height;
	private boolean update;
	private int active;
	
	public TileGrid(String[] paths, String inLabel, int[] inCodes, int gridHeight) {
		height = gridHeight;
		label = inLabel;
		active = 0;
		icons = new GridIcon[paths.length];
		for(int i = 0; i < paths.length; i++) {
			icons[i] = new GridImage(paths[i], inCodes[i]);
		}
	}
	
	protected TileGrid(String inLabel, int inHeight) {
		label = inLabel;
		height = inHeight;
		icons = new GridIcon[0];
	}

	public boolean dragTileProcess(int code, int x, int y) {
		//TODO: Figure out how to make this rearrange colors in current pallet
		return false;
	}
	
	public void setActive(int in) {
		in = in < 0 ? 0 : in;
		in = in >= icons.length ? icons.length - 1 : in;
		getGridIcons()[active].toggleSelected();
		active = in;
		getGridIcons()[active].toggleSelected();
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		if(update) {
			p.removeElementPrefixed("gr_" + label);
		}
		p.handleText("gr_" + label, false, x + getTileWidth() / 2, y + getHeight() * 5 / 8, getTileWidth(), getHeight() * 3 / 8, SMALL_LABEL_FONT, label);
		int posX = x;
		int posY = y - getHeight() / (height + 1);
		int size = getHeight() / (height + 1);
		for(int i = 0; i < icons.length; i++) {
			if(i % (height) == 0 && i != 0) {
				posX += size;
				posY = y - size;
			}
			icons[i].draw(p, "gr_" + label, posX, posY, i, size);
			
			posY += size;
		}
	}

	@Override
	public int getTileWidth() {
		int useWid = icons.length;
		useWid = useWid == 0 ? 2 : useWid;
		return getHeight() / (height + 1) * (useWid / (height));
	}
	
	protected GridIcon[] getGridIcons() {
		return icons;
	}
	
	protected void assignGridIcons(GridIcon[] in) {
		icons = in;
		update = true;
	}
	
	@Override
	public String getInfo() {
		return ""+active;
	}
	
	protected int getActive() {
		return active;
	}

	public ArrayList<Integer> getAssociatedCodes(){
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(GridIcon gI : icons) {
			out.add(gI.getCode());
		}
		return out;
	}
}
