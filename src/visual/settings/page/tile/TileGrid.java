package visual.settings.page.tile;

import visual.composite.HandlePanel;

public class TileGrid extends Tile{

	private String[][] imagePaths;	//Make helper class to represent each 'icon'
	private int[][] codes;
	private boolean border;
	private String label;
	
	public TileGrid(String[][] paths, String inLabel, int[][] inCodes) {
		imagePaths = paths;
		label = inLabel;
		codes = inCodes;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		int posX = x;
		int posY = y;
		
		for(int i = 0; i < imagePaths.length; i++) {
			for(int j = 0; j < imagePaths[i].length; j++) {
				
			}
		}
	}

	@Override
	public int getTileWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
