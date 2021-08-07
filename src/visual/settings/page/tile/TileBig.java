package visual.settings.page.tile;

import java.awt.Color;
import java.util.ArrayList;

import visual.composite.HandlePanel;

public class TileBig extends Tile{

//---  Instance Variables   -------------------------------------------------------------------
	
	private String img;
	private String label;
	private int code;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public TileBig(String inImg, String display, int inCode) {
		img = inImg;
		label = display;
		code = inCode;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		x += getTileWidth() / 2;
		int wid = getTileWidth();
		int posY =  y - getHeight() / 5;
		p.handleImage("tile_big_img_" + label + "_" + code, "move", 12, x, posY, wid * 3 / 4, wid * 3 / 4, img);
		p.handleButton("tile_big_butt_" + label + "_" + code, "move", 15, x, y, wid, getHeight() , code);
		p.handleText("tile_big_txt_" + label + "_" + code, "move", 15, x, y + getHeight() / 4, wid, getHeight(), SMALL_LABEL_FONT, label);
		p.handleRectangle("rect_test_" + label + "_" + code, "move", 10,  x, y, wid, getHeight(), Color.white, Color.black);
	}

	@Override
	public boolean dragTileProcess(int code, int x, int y) {
		return false;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	@Override
	public int getTileWidth() {
		return getHeight() * 4 / 5;
	}

	public String getInfo() {
		return "";
	}
	
	public ArrayList<Integer> getAssociatedCodes(){
		ArrayList<Integer> out = new ArrayList<Integer>();
		out.add(code);
		return out;
	}
	
}
