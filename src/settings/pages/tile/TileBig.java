package settings.pages.tile;

import java.awt.Color;

import settings.pages.Page;

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
	public void drawTile(int x, int y) {
		x += getTileWidth() / 2;
		Page p = getPage();
		int wid = getTileWidth();
		p.handleImage("tile_big_img_" + label + "_" + x, x, y - height / 5, img, 1);
		p.handleButton("tile_big_butt_" + label + "_" + x, x, y - height / 5, wid, height , code);
		p.handleText("tile_big_txt_" + label + "_" + x, x, y + height / 4, wid, height, label);
		p.handleRectangle("rect_test_" + label + "_" + x, x, y, wid, height, Color.white, Color.black);
	}

	@Override
	public int getTileWidth() {
		return height * 4 / 5;
	}

}
