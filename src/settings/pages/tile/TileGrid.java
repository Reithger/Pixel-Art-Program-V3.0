package settings.pages.tile;

public class TileGrid extends Tile{

	private String[][] grid;
	private boolean border;
	private String label;
	
	public TileGrid(String[][] inGr) {
		grid = inGr;
	}
	
	@Override
	public void drawTile(int x, int y) {
		int posX = x;
		int posY = y;
		
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				
			}
		}
	}

	@Override
	public int getTileWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
