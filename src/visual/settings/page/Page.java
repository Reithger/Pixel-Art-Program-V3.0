package visual.settings.page;

import java.awt.Color;
import java.util.ArrayList;

import visual.composite.HandlePanel;
import visual.settings.SettingsBar;
import visual.settings.page.tile.Tile;
import visual.settings.page.tile.TileFactory;

public abstract class Page extends HandlePanel{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CODE_BACK_SCROLL = -47;
	private final static int CODE_FORWARD_SCROLL = -48;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Tile> tiles;
	private String name;
	private static SettingsBar reference;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		tiles = new ArrayList<Tile>();
		this.setScrollBarVertical(false);
		setScrollBarHorizontal(false);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignReference(SettingsBar ref) {
		reference = ref;
	}
	
	public void addTileBig(String label, String path, int code) {
		addTile(TileFactory.generateTileBig(path, label, code));
	}
	
	public void addTileGrid(String[][] paths, String label, int[][] codes) {
		addTile(TileFactory.generateTileGrid(paths, label, codes));
	}
	
	private void addTile(Tile in) {
		tiles.add(in);
		in.assignMaximumVerticalSpace(getHeight());
	}
	
	public void drawPage() {
		int buffer = getWidth() / 50;
		int posX = buffer / 2;
		int posY = getHeight() / 2;

		removeElementPrefixed("navigate");
		
		for(Tile t : tiles) {
			handleLine("line_" + posX, false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
			posX += buffer;
			t.drawTile(posX, posY, this);
			posX += t.getTileWidth() + buffer;
		}
		this.handleLine("line_" + posX, false, 10, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
		
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
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
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
