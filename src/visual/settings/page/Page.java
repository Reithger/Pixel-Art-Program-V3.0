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

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Tile> tiles;
	private HashMap<Integer, String> tileCodes;
	private String name;
	private static InputHandler reference;
	
	private volatile boolean mutexHere;
	
	private boolean dragging;
	private int lastX;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		tiles = new HashMap<String, Tile>();
		tileCodes = new HashMap<Integer, String>();
		setScrollBarVertical(false);
		setScrollBarHorizontal(false);
		getPanel().setBackground(null);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignReference(InputHandler ref) {
		reference = ref;
	}

	public void drawPage() {
		openLockHere();
		int buffer = getWidth() / 50;
		int posX = buffer / 2;
		int posY = getHeight() / 2;
		
		ArrayList<Tile> disp = new ArrayList<Tile>(tiles.values());
		Collections.sort(disp);
		for(int i = 0; i < disp.size(); i++) {
			handleLine("line_" + i, false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
			posX += buffer;
			Tile t = disp.get(i);
			t.drawTile(posX, posY, this);
			posX += t.getTileWidth() + buffer;
		}
		handleLine("line_" + disp.size(), false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
		
		handleThickRectangle("outline", true, 0, 0, getWidth(), getHeight(), Color.black, 2);
		closeLockHere();
	}
	
	private void openLockHere() {
		while(mutexHere) {};
		mutexHere = true;
	}
	
	private void closeLockHere() {
		mutexHere = false;
	}
	
	public void refresh(boolean pushUpdate) {
		refreshLocal(pushUpdate);
	}
	
	protected abstract void refreshLocal(boolean pushUpdate);
	
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
		openLockHere();
		TileFactory.updateTileGridColors(getTile(ref), cols, codeStart);
		updateCodeAssociations(getTile(ref));
		closeLockHere();
	}
	
	public void assignTileGridImages(String ref, ArrayList<String> paths, int[] codes) {
		openLockHere();
		TileFactory.updateTileGridImages(getTile(ref), paths, codes);
		updateCodeAssociations(getTile(ref));
		closeLockHere();
	}
	
	public void assignTileGridActive(String ref, int inde) {
		openLockHere();
		TileFactory.updateTileGridActive(getTile(ref), inde);
		closeLockHere();
	}
	
	public void assignTileNumericSelectorValues(String ref, int min, int max, int store) {
		openLockHere();
		TileFactory.updateTileNumericSelectorValues(getTile(ref), min, max, store);
		closeLockHere();
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
		reference.handleKeyInput(code);
	}
	
	@Override
	public void clickPressBehaviour(int code, int x, int y) {
		lastX = x;
		if(code == -1) {
			dragging = true;
		}
	}
	
	@Override
	public void clickReleaseBehaviour(int code, int x, int y) {
		dragging = false;
	}
	
	@Override
	public void dragBehaviour(int code, int x, int y) {
		System.out.println("D: " + code);
		if(dragging) {
			setOffsetXBounded(getOffsetX() + x - lastX);
			lastX = x;
		}
		else if(tileCodes.get(code) != null && getTile(tileCodes.get(code)).dragTileProcess(code, x - getOffsetX(), y - getOffsetY())) {
			refresh(true);
		}
	}
	
	@Override
	public void clickBehaviour(int code, int x, int y) {
		System.out.println("C: " + code);
		reference.handleCodeInput(code, tileCodes.get(code));
		refresh(false);
		drawPage();
	}
	
}
