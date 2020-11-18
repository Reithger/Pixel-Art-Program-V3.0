package visual.settings.page.tile;

public class TileFactory {
	
	public final static String TILE_TYPE_BIG = "B";
	public final static String TILE_TYPE_GRID = "G";

	public static TileBig generateTileBig(String path, String label, int code) {
		return new TileBig(path, label, code);
	}
	
	public static TileGrid generateTileGrid(String[][] paths, String label, int[][] code) {
		return new TileGrid(paths, label, code);
	}
	
}
