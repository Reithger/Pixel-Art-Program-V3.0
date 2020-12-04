package visual.settings.page.tile.grid;

import java.awt.Color;
import java.util.ArrayList;

import control.CodeReference;
import visual.composite.HandlePanel;
import visual.settings.page.tile.Tile;

public class TileGrid extends Tile{

	//TODO: Hover-text labels for each button
	private GridImage DEFAULT_EMERGENCY = new GridImage("./assets/placeholder.png", -1);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private GridIcon[] icons;
	private String label;
	private int height;
	private boolean update;
	private int active;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public TileGrid(String[] paths, String inLabel, int[] inCodes, int gridHeight) {
		height = gridHeight;
		label = inLabel;
		active = 0;
		icons = new GridIcon[paths.length];
		for(int i = 0; i < paths.length; i++) {
			icons[i] = new GridImage(paths[i], inCodes[i]);
		}
	}
	
	public TileGrid(String inLabel, int inHeight) {
		label = inLabel;
		height = inHeight;
		icons = new GridIcon[0];
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public boolean dragTileProcess(int code, int x, int y) {
		//TODO: Figure out how to make this rearrange colors in current pallet
		return false;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		if(update) {
			p.removeElementPrefixed("gr_" + label);
		}
		p.handleText("gr_" + label, false, x + calculateTileWidth(), y + getHeight() * 7 / 16, getTileWidth() * 2, getHeight() * 3 / 8, SMALL_LABEL_FONT, label);
		int size = calculateTileWidth();
		int posX = x + size / 2;
		int posY = y - size;
		
		if(icons.length > getActive()) {
			icons[getActive()].draw(p, "gr_" + label + "_big", posX, y, size * 2);
		}
		else {
			DEFAULT_EMERGENCY.draw(p, "gr_" + label + "_big", posX, y, size * 2);
		}
		
		posX += size * 2;
		
		for(int i = 0; i < icons.length; i++) {
			if(i % (height) == 0 && i != 0) {
				posX += size;
				posY = y - size;
			}
			icons[i].draw(p, "gr_" + label, posX, posY, size);
			
			posY += size;
		}
	}

	public void updateGridIconsImage(ArrayList<String> imagePaths, int[] codes) {
		icons = new GridIcon[imagePaths.size()];
		for(int i = 0; i < imagePaths.size(); i++) {
			icons[i] = new GridImage(imagePaths.get(i), codes[i]);
		}
	}
	
	public void updateGridIconsColor(ArrayList<Color> cols, int[] codeSt) {
		icons = new GridIcon[cols.size() + 1];
		for(int i = 0; i < cols.size(); i++) {
			icons[i] = new GridColor(cols.get(i), codeSt[i], i == getActive());
		}
		//TODO: Need to check for max # and not provide adder in that case
		icons[cols.size()] = new GridColor(Color.white, CodeReference.CODE_COLOR_ADD, false);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	protected void assignGridIcons(GridIcon[] in) {
		icons = in;
		update = true;
	}
	
	public void setActive(int in) {
		if(in == active) {
			return;
		}
		in = in < 0 ? 0 : in;
		in = in >= icons.length ? icons.length - 1 : in;
		getGridIcons()[active].toggleSelected();
		active = in;
		getGridIcons()[active].toggleSelected();
		update = true;
	}

//---  Getter Methods   -----------------------------------------------------------------------

	@Override
	public int getTileWidth() {
		int useWid = icons.length;
		useWid = useWid < 6 ? 6 : useWid;
		return (int)(calculateTileWidth() * (2.5 + useWid / (height)));
	}
	
	private int calculateTileWidth() {
		return (getHeight() / (height + 1));
	}
	
	protected GridIcon[] getGridIcons() {
		return icons;
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
