package settings.pages.tile;

public class TileFactory {

	public static TileBig generateTileBig(String path, String label, int code) {
		return new TileBig(path, label, code);
	}
	
	public static TileGrid generateTileGrid(String[][] grid) {
		return new TileGrid(grid);
	}
	
}
