package visual.settings.page;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import control.CodeReference;
import control.InputHandler;
import input.CustomEventReceiver;
import input.manager.actionevent.KeyActionEvent;
import visual.composite.HandlePanel;
import visual.settings.page.tile.Tile;
import visual.settings.page.tile.TileFactory;

public abstract class Page extends HandlePanel implements InputHandler{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int DEFAULT_NEGATIVE_CODE = -57;
	private final static Font HOVER_TEXT_FONT = new Font("Serif", Font.BOLD, 16);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Tile> tiles;
	private HashMap<Integer, String> tileCodes;
	private String name;
	private static InputHandler reference;
	
	private volatile boolean mutexHere;
	
	private boolean tileDrag;
	private int draggedCode;
	private boolean dragging;
	private int lastX;
	private static boolean displayTooltip;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		displayTooltip = true;
		tiles = new HashMap<String, Tile>();
		tileCodes = new HashMap<Integer, String>();
		setScrollBarVertical(false);
		setScrollBarHorizontal(false);
		getPanel().setBackground(null);
		Page p = this;
		setEventReceiver(new CustomEventReceiver() {
			private Page use = p;
			
			@Override
			public void keyReleaseEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_UP);
				
			}
			
			@Override
			public void keyPressEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_DOWN);
			}
			
			@Override
			public void keyEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY);
			}
			
			@Override
			public void clickPressEvent(int code, int x, int y, int clickStart) {
				lastX = x;
				dragging = false;
				resolveTileDrag();
				if(code == -1) {
					dragging = true;
				}
				else if(tileCodes.get(tileDrag ? draggedCode : code) != null){
					tileDrag = true;
					draggedCode = code;
				}
			}
			
			@Override
			public void clickReleaseEvent(int code, int x, int y, int clickStart) {
				dragging = false;
				resolveTileDrag();
				refresh();
			}
			
			@Override
			public void dragEvent(int code, int x, int y, int clickStart) {
				if(dragging) {
					setOffsetXBounded(getOffsetX() + x - lastX);
					lastX = x;
				}
				else if(tileDrag && getTile(tileCodes.get(draggedCode)).dragTileProcess(tileDrag ? draggedCode : code, x - getOffsetX(), y - getOffsetY())) {
					getTile(tileCodes.get(tileDrag ? draggedCode : code)).drawTileMemory(use);
				}
			}
			
			@Override
			public void clickEvent(int code, int x, int y, int clickStart) {
				resolveTileDrag();
				reference.handleCodeInput(code, tileCodes.get(code));
				drawPage();
			}
			
			private long hoverTextWait;
			
			@Override
			public void mouseMoveEvent(int code, int x, int y) {
				if(displayTooltip && tileCodes.get(code) != null) {
					if(hoverTextWait == -1) {
						hoverTextWait = System.currentTimeMillis();
					}
					else if(System.currentTimeMillis() - hoverTextWait > 250) {
						String disp = getTile(code).getTooltipText(code);
						if(!disp.equals("")) {
							int posX = x;
							int posY = y;
							int wid = use.getTextWidth(disp, HOVER_TEXT_FONT) + 3;
							int hei = use.getTextHeight(HOVER_TEXT_FONT);
							posX = ((posX - wid) < 0 ? posX : (posX - wid));
							posY = ((posY - hei) < 0 ? posY : (posY - hei));
							posX += (posX == x) ? 15 : 0;
							use.addText("tooltip_overlay_txt", 100, false, posX, posY, wid, hei, disp, HOVER_TEXT_FONT, true, true, false);
							use.addRectangle("tooltip_overlay_rect", 99, false, posX + wid / 2, posY + hei / 2, wid + 4, hei, true, Color.white, Color.black);
						}
					}
				}
				else {
					hoverTextWait = -1;
					use.removeElementPrefixed("tooltip_overlay");
				}
			}

			private void resolveTileDrag() {
				if(tileDrag) {
					pushChanges();
				}
				tileDrag = false;
				draggedCode = DEFAULT_NEGATIVE_CODE;
			}
			
		});
		
		
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
	
	public void refresh() {
		for(Tile t : tiles.values()) {
			if(t.getRefreshCode() != null) {
				handleCodeInput(t.getRefreshCode(), t.getReference());
			}
		}
	}
	
	public void pushChanges() {
		for(Tile t : tiles.values()) {
			if(t.getPushChangeCode() != null) {
				handleCodeInput(t.getPushChangeCode(), t.getReference());
			}
		}
		handleCodeInput(CodeReference.CODE_PERFORM_REFRESH, null);
	}
	
	//-- Input  -----------------------------------------------
	
	public void handleCodeInput(int code, String context) {
		reference.handleCodeInput(code, context);
	}
	
	public void handleDrawInput(int x, int y, int duration, String ref) {
		reference.handleDrawInput(x, y, duration, ref);
	}
	
	public void handleKeyInput(char code, int keyType) {
		reference.handleKeyInput(code, keyType);
	}

	//-- Tiles  -----------------------------------------------
	
	public void addTileBig(String ref, Integer refresh, Integer push, String label, String path, int code) {
		addTile(ref, refresh, push, TileFactory.generateTileBig(path, label, code));
	}
	
	public void addTileGrid(String ref, Integer refresh, Integer push, String label, int gridHeight, boolean showSelection) {
		addTile(ref, refresh, push, TileFactory.generateTileGrid(label, gridHeight, showSelection));
	}
	
	public void addTileNumericSelector(String ref, Integer refresh, Integer push, String label, int minVal, int maxVal, int decCode, int incCode, int setCode) {
		addTile(ref, refresh, push, TileFactory.generateTileNumericSelector(label, minVal, maxVal, decCode, incCode, setCode));
	}
	
	private void addTile(String ref, Integer refresh, Integer push, Tile in) {
		in.setPriority(tiles.size());
		in.setTileMetaInfo(ref, refresh, push);
		updateCodeAssociations(in);
		//TODO: Complexity for removing/moving tiles?
		tiles.put(ref, in);
		in.assignMaximumVerticalSpace(getHeight());
	}
	
	private void updateCodeAssociations(Tile in) {
		for(int i : in.getAssociatedCodes()) {
			tileCodes.put(i, in.getReference());
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void toggleTooltips() {
		displayTooltip = !displayTooltip;
	}
	
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
	
	private Tile getTile(int code) {
		return getTile(tileCodes.get(code));
	}
	
	public String getTileInfo(String ref) {
		return getTile(ref).getInfo();
	}
	
}
