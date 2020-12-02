package visual.settings.page.tile;

import java.awt.Color;
import java.util.ArrayList;

import visual.settings.page.tile.grid.TileColorGrid;
import visual.settings.page.tile.grid.TileGrid;

public class TileFactory {
	
	public final static String TILE_TYPE_BIG = "B";
	public final static String TILE_TYPE_GRID = "G";
	
	public static TileBig generateTileBig(String path, String label, int code) {
		return new TileBig(path, label, code);
	}
	
	public static TileGrid generateTileGrid(String[] paths, String label, int[] code, int gridHeight) {
		return new TileGrid(paths, label, code, gridHeight);
	}
	
	public static TileColorGrid generateTileColorGrid(String label, int height) {
		return new TileColorGrid(label, height);
	}
	
	public static void updateTileColorGridActive(Tile in, int active) {
		TileColorGrid tCG = castTileColorGrid(in);
		if(tCG != null) {
			tCG.setActive(active);
		}
	}
	
	public static void updateTileColorGrid(Tile in, ArrayList<Color> cols, int codeStart) {
		TileColorGrid tCG = castTileColorGrid(in);
		if(tCG != null) {
			tCG.assignColors(cols, codeStart);
		}
	}
	
	private static TileColorGrid castTileColorGrid(Tile in) {
		try {
			return ((TileColorGrid)in);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
