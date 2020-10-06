package settings.pages;

import java.awt.Color;
import java.util.ArrayList;

import meta.HandlePanel;
import settings.pages.tile.Tile;

public abstract class Page extends HandlePanel{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CODE_BACK_SCROLL = -47;
	private final static int CODE_FORWARD_SCROLL = -48;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Tile> tiles;
	private String name;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Page(String inName) {
		super(0, 0, 100, 100);
		name = inName;
		tiles = new ArrayList<Tile>();
		this.setScrollBarVertical(false);
		setScrollBarHorizontal(false);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addTile(Tile in) {
		tiles.add(in);
		in.assignPage(this);
	}
	
	public void drawPage() {
		int buffer = getWidth() / 50;
		int posX = buffer / 2;
		int posY = getHeight() / 2;

		removeElementPrefixed("navigate");
		
		for(Tile t : tiles) {
			handleLine("line_" + posX, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
			posX += buffer;
			t.drawTile(posX, posY);
			posX += t.getTileWidth() + buffer;
		}
		this.handleLine("line_" + posX, posX, getHeight() / 8, posX, getHeight() * 7 / 8, 1, Color.black);
		
		setFrameMode(true);
		
		handleThickRectangle("outline", 0, 0, getWidth(), getHeight(), Color.black, 2);
		
		if(getMaximumScreenX() > getWidth()) {
			int size = getWidth() / 30;
			posX = buffer;
			if(getWidth() - getOffsetX() < getMaximumScreenX()) {
				handleButton("navigate_butt_forward", getWidth() - posX, posY + getHeight() / 4, size, size, CODE_FORWARD_SCROLL);
				handleRectangle("navigate_rect_forward", getWidth() - posX, posY + getHeight() / 4, size, size, Color.white, Color.black);
			}
			if(getOffsetX() != 0) {
				handleButton("navigate_butt_back", posX, posY + getHeight() / 4, size, size, CODE_BACK_SCROLL);
				handleRectangle("navigate_rect_back", posX, posY + getHeight() / 4, size, size, Color.white, Color.black);
			}
		}
		
		setFrameMode(false);
	}
	
	public abstract void processInput(int code);

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
		processInput(code);
		drawPage();
	}
	
}
