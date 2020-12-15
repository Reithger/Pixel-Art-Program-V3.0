package visual.settings.page.tile.grid;

import java.awt.Color;
import java.util.ArrayList;

import visual.composite.HandlePanel;
import visual.settings.page.tile.Tile;

public class TileGrid extends Tile{

//---  Constants   ----------------------------------------------------------------------------
	
	//TODO: Hover-text labels for each button
	private GridImage DEFAULT_EMERGENCY = new GridImage("./assets/placeholder.png", -1);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private GridIcon[] icons;
	private String label;
	private int height;
	private boolean update;
	private int active;
	private boolean showSelection;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public TileGrid(String inLabel, int inHeight, boolean show) {
		label = inLabel;
		height = inHeight;
		icons = new GridIcon[0];
		showSelection = show;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public boolean dragTileProcess(int code, int x, int y) {
		//TODO: Figure out how to make this rearrange colors in current pallet
		return false;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		logPosition(x, y);
		if(update) {
			p.removeElementPrefixed("gr_" + label);
		}
		int size = calculateTileWidth();
		int posX = x + (showSelection ? size / 2 : 0);
		int defaultY = y - size - getHeight() / 16;
		int posY = defaultY;
		
		drawLabel(label, x, y, p);
		
		if(showSelection) {
			if(icons.length > getActive()) {
				icons[getActive()].draw(p, "gr_" + label + "_big", posX, y, size * 2);
			}
			else {
				DEFAULT_EMERGENCY.draw(p, "gr_" + label + "_big", posX, y, size * 2);
			}
			posX += size * 2;
		}
		
		
		for(int i = 0; i < icons.length; i++) {
			if(i % (height) == 0 && i != 0) {
				posX += size;
				posY = defaultY;
			}
			icons[i].draw(p, "gr_" + label, posX, posY, size);
			
			posY += size;
		}
	}

	public void updateGridIconsImage(ArrayList<String> imagePaths, int[] codes) {
		GridIcon[] newIcons = new GridIcon[imagePaths.size()];
		for(int i = 0; i < imagePaths.size(); i++) {
			newIcons[i] = new GridImage(imagePaths.get(i), codes[i]);
		}
		assignGridIcons(newIcons);
	}
	
	public void updateGridIconsColor(ArrayList<Color> cols, int[] codeSt) {
		GridIcon[] newIcons = new GridIcon[cols.size()];
		for(int i = 0; i < cols.size(); i++) {
			newIcons[i] = new GridColor(cols.get(i), codeSt[i]);
		}
		assignGridIcons(newIcons);
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	protected void assignGridIcons(GridIcon[] in) {
		boolean change = icons.length != in.length;
		if(!change) {
			for(int i = 0; i < in.length; i++) {
				change = change || !in[i].equals(icons[i]);
			}
		}
		icons = in;
		update = change;
	}
	
	public void setActive(int in) {
		if(in == active) {
			return;
		}
		in = in < 0 ? 0 : in;
		in = in >= icons.length ? icons.length - 1 : in;
		if(active < icons.length)
			getGridIcons()[active].toggleSelected();
		active = in;
		getGridIcons()[active].toggleSelected();
		update = true;
	}

//---  Getter Methods   -----------------------------------------------------------------------

	@Override
	public int getTileWidth() {
		int useWid = icons.length;
		useWid = useWid < 3 ? 3 : useWid;
		return (int)(calculateTileWidth() * ((showSelection ? 2.5 : .5) + useWid / (height)));
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
