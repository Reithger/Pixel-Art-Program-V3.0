package visual.settings.page.tile;

import java.awt.Color;

import visual.composite.HandlePanel;

public class TileGrid extends Tile{

	private String[] imagePaths;	//Make helper class to represent each 'icon'
	private int[] codes;	//1D array, 'height' tall columns
	private boolean border;
	private String label;
	private int height;
	
	public TileGrid(String[] paths, String inLabel, int[] inCodes, int gridHeight) {
		imagePaths = paths;
		height = gridHeight;
		label = inLabel;
		codes = inCodes;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		p.handleText("gr_" + x + "_" + y, false, x + getTileWidth() / 2, y + getHeight() * 5 / 8, getTileWidth(), getHeight() * 3 / 8, SMALL_LABEL_FONT, label);
		int posX = x;
		int posY = y - getHeight() / (height + 1);
		int size = getHeight() / (height + 1);
		
		for(int i = 0; i < imagePaths.length; i++) {
			if(i % (height) == 0 && i != 0) {
				posX += size;
				posY = y - size;
			}
			p.handleRectangle("gr_r_" + posX + "_" + posY + "_" + i, false, 5, posX, posY, size, size, Color.white, Color.black);
			
			p.handleImageButton("gr_" + posX + "_" + posY + "_" + i, false, posX, posY, size, size, imagePaths[i], codes[i]);
			
			posY += size;
		}
	}

	@Override
	public int getTileWidth() {
		return getHeight() / 4 * (imagePaths.length / 3);
	}

}
