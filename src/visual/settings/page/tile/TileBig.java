package visual.settings.page.tile;

import java.awt.Color;
import java.util.ArrayList;

import visual.composite.HandlePanel;

public class TileBig extends Tile{

	private String img;
	private String label;
	private int code;
	
	public TileBig(String inImg, String display, int inCode) {
		img = inImg;
		label = display;
		code = inCode;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		x += getTileWidth() / 2;
		int wid = getTileWidth();
		int posY =  y - getHeight() / 5;
		p.handleImage("tile_big_img_" + label + "_" + code, false, x, posY, img, 1);
		p.handleButton("tile_big_butt_" + label + "_" + code, false,  x, posY, wid, getHeight() , code);
		p.handleText("tile_big_txt_" + label + "_" + code, false,  x, y + getHeight() / 4, wid, getHeight(), SMALL_LABEL_FONT, label);
		p.handleRectangle("rect_test_" + label + "_" + code, false, 10,  x, y, wid, getHeight(), Color.white, Color.black);
	}

	public boolean dragTileProcess(int code, int x, int y) {
		return false;
	}
	
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
