package visual.settings.page.tile;

import java.awt.Color;
import java.util.ArrayList;

public class TileFactory {
	
//---  Constants   ----------------------------------------------------------------------------
	
	public final static String TILE_TYPE_BIG = "B";
	public final static String TILE_TYPE_GRID = "G";
	
//---  Generate Methods   ---------------------------------------------------------------------
	
	public static TileNumericSelector generateTileNumericSelector(String label, int minVal, int maxVal, int decCode, int incCode, int setCode) {
		return new TileNumericSelector(label, minVal, maxVal, decCode, incCode, setCode);
	}
	
	public static TileBig generateTileBig(String path, String label, int code) {
		return new TileBig(path, label, code);
	}
	
	public static TileGrid generateTileGrid(String label,  int gridHeight, boolean showSelection) {
		return new TileGrid(label, gridHeight, showSelection);
	}
	
//---  Update Methods   -----------------------------------------------------------------------
	
	public static void updateTileGridActive(Tile in, int active) {
		TileGrid tCG = castTileGrid(in);
		if(tCG != null) {
			tCG.setActive(active);
		}
	}
	
	public static void updateTileGridImages(Tile in, ArrayList<String> paths, int[] codes) {
		TileGrid tCG = castTileGrid(in);
		if(tCG != null) {
			tCG.updateGridIconsImage(paths, codes);
		}
	}
	
	public static void updateTileGridColors(Tile in, ArrayList<Color> cols, int[] codeStart) {
		TileGrid tCG = castTileGrid(in);
		if(tCG != null) {
			tCG.updateGridIconsColor(cols, codeStart);
		}
	}
	
	public static void updateTileNumericSelectorValues(Tile in, int min, int max, int stored) {
		TileNumericSelector tNS = castTileNumericSelector(in);
		if(tNS != null) {
			tNS.setValues(min, max, stored);
		}
	}
	
//---  Casting Methods   ----------------------------------------------------------------------
	
	private static TileGrid castTileGrid(Tile in) {
		try {
			return ((TileGrid)in);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static TileNumericSelector castTileNumericSelector(Tile in) {
		try {
			return ((TileNumericSelector)in);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
