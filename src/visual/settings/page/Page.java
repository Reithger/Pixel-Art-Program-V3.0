package visual.settings.page;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import control.InputHandler;
import visual.composite.HandlePanel;
import visual.settings.page.tile.Tile;
import visual.settings.page.tile.TileFactory;

public abstract class Page extends HandlePanel implements InputHandler{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CODE_BACK_SCROLL = -47;
	private final static int CODE_FORWARD_SCROLL = -48;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Tile> tiles;
	private HashMap<Integer, String> tileCodes;
	private String name;
	private static InputHandler reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		tiles = new HashMap<String, Tile>();
		tileCodes = new HashMap<Integer, String>();
		this.setScrollBarVertical(false);
		setScrollBarHorizontal(false);
		getPanel().setBackground(null);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignReference(InputHandler ref) {
		reference = ref;
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

	public abstract void refresh();
	
	//-- Input  -----------------------------------------------
	
	public void handleCodeInput(int code, String context) {
		reference.handleCodeInput(code, context);
	}
	
	public void handleDrawInput(int x, int y, int duration, String ref) {
		reference.handleDrawInput(x, y, duration, ref);
	}
	
	public void handleKeyInput(char code) {
		reference.handleKeyInput(code);
	}

	//-- Tiles  -----------------------------------------------
	
	public void addTileBig(String ref, String label, String path, int code) {
		addTile(ref, TileFactory.generateTileBig(path, label, code));
	}
	
	public void addTileGrid(String ref, String label, int gridHeight) {
		addTile(ref, TileFactory.generateTileGrid(label, gridHeight));
	}
	
	public void addTileNumericSelector(String ref, String label, int minVal, int maxVal, int decCode, int incCode, int setCode) {
		addTile(ref, TileFactory.generateTileNumericSelector(label, minVal, maxVal, decCode, incCode, setCode));
	}
	
	private void addTile(String ref, Tile in) {
		in.setPriority(tiles.size());
		in.setName(ref);
		updateCodeAssociations(in);
		//TODO: Complexity for removing/moving tiles?
		tiles.put(ref, in);
		in.assignMaximumVerticalSpace(getHeight());
	}
	
	private void updateCodeAssociations(Tile in) {
		for(int i : in.getAssociatedCodes()) {
			tileCodes.put(i, in.getName());
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void assignTileGridColors(String ref, ArrayList<Color> cols, int[] codeStart) {
		TileFactory.updateTileGridColors(getTile(ref), cols, codeStart);
		updateCodeAssociations(getTile(ref));
	}
	
	public void assignTileGridImages(String ref, ArrayList<String> paths, int[] codes) {
		TileFactory.updateTileGridImages(getTile(ref), paths, codes);
		updateCodeAssociations(getTile(ref));
	}
	
	public void assignTileGridActive(String ref, int inde) {
		TileFactory.updateTileGridActive(getTile(ref), inde);
	}
	
	public void assignTileNumericSelectorValues(String ref, int min, int max, int store) {
		TileFactory.updateTileNumericSelectorValues(getTile(ref), min, max, store);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	private Tile getTile(String ref) {
		return tiles.get(ref);
	}
	
	public String getTileInfo(String ref) {
		return getTile(ref).getInfo();
	}
	
//---  Reactions   ----------------------------------------------------------------------------
	
	@Override
	public void keyBehaviour(char code) {
		
	}
	
	@Override
	public void dragBehaviour(int code, int x, int y) {
		if(tileCodes.get(code) == null) {
			return;
		}
		if(getTile(tileCodes.get(code)).dragTileProcess(code, x, y)) {
			refresh();
		}
		else {
			//Meta movement
		}
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
				reference.handleCodeInput(code, tileCodes.get(code));
				refresh();
				break;
		}
		drawPage();
	}
	
}
