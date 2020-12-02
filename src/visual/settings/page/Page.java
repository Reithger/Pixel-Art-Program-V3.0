package visual.settings.page;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import control.CodeReference;
import visual.composite.HandlePanel;
import visual.settings.SettingsBar;
import visual.settings.page.tile.Tile;
import visual.settings.page.tile.TileFactory;

public abstract class Page extends HandlePanel{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CODE_BACK_SCROLL = -47;
	private final static int CODE_FORWARD_SCROLL = -48;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Tile> tiles;
	private String name;
	private static SettingsBar reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		tiles = new HashMap<String, Tile>();
		this.setScrollBarVertical(false);
		setScrollBarHorizontal(false);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignReference(SettingsBar ref) {
		reference = ref;
	}
	
	public void addTileBig(String ref, String label, String path, int code) {
		addTile(ref, TileFactory.generateTileBig(path, label, code));
	}
	
	public void addTileGrid(String ref, String[] paths, String label, int[] codes, int gridHeight) {
		addTile(ref, TileFactory.generateTileGrid(paths, label, codes, gridHeight));
	}
	
	public void addTileColorGrid(String ref, String label, int height) {
		addTile(ref, TileFactory.generateTileColorGrid(label, height));
	}
	
	private void addTile(String ref, Tile in) {
		tiles.put(ref, in);
		in.assignMaximumVerticalSpace(getHeight());
	}
	
	public void drawPage() {
		int buffer = getWidth() / 50;
		int posX = buffer / 2;
		int posY = getHeight() / 2;

		removeElementPrefixed("navigate");
		ArrayList<Tile> disp = new ArrayList<Tile>(tiles.values());
		Collections.sort(disp);
		for(int i = 0; i < disp.size(); i++) {
			handleLine("line_" + i, false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
			posX += buffer;
			Tile t = disp.get(i);
			t.drawTile(posX, posY, this);
			posX += t.getTileWidth() + buffer;
		}
		this.handleLine("line_" + disp.size(), false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
		
		handleThickRectangle("outline", true, 0, 0, getWidth(), getHeight(), Color.black, 2);
		
		if(getMaximumScreenX() > getWidth()) {
			int size = getWidth() / 30;
			posX = buffer;
			if(getWidth() - getOffsetX() < getMaximumScreenX()) {
				handleButton("navigate_butt_forward", true, getWidth() - posX, posY + getHeight() / 4, size, size, CODE_FORWARD_SCROLL);
				handleRectangle("navigate_rect_forward", true, 15, getWidth() - posX, posY + getHeight() / 4, size, size, Color.white, Color.black);
			}
			if(getOffsetX() != 0) {
				handleButton("navigate_butt_back", true, posX, posY + getHeight() / 4, size, size, CODE_BACK_SCROLL);
				handleRectangle("navigate_rect_back", true, 15, posX, posY + getHeight() / 4, size, size, Color.white, Color.black);
			}
		}
		
	}
	
	protected void passCodeInput(int code) {
		reference.passInputCode(code);
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	public void assignTileColorGridColors(String ref, ArrayList<Color> cols, int codeStart) {
		TileFactory.updateTileColorGrid(getTile(ref), cols, codeStart + CodeReference.CODE_RANGE_SELECT_COLOR);
	}
	
	public void assignTileColorGridActive(String ref, int inde) {
		TileFactory.updateTileColorGridActive(getTile(ref), inde);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	private Tile getTile(String ref) {
		return tiles.get(ref);
	}
	
//---  Reactions   ----------------------------------------------------------------------------
	
	@Override
	public void keyBehaviour(char code) {
		
	}
	
	@Override
	public void clickBehaviour(int code, int x, int y) {
		switch(code) {
			case CODE_BACK_SCROLL:
				setOffsetX(getOffsetX() + getWidth() - 100);
				break;
			case CODE_FORWARD_SCROLL:
				setOffsetX(getOffsetX() - (getWidth() - 100));
				break;
			default:
				break;
		}
		reference.passOnCode(code);
		drawPage();
	}
	
}
