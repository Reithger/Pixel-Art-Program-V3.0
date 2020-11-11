package visual.settings.page;

import control.CodeReference;
import visual.settings.page.tile.TileFactory;

public class FilePage extends Page{

	private final static String PAGE_NAME = "File";
	private final static String[][] BUTTONS = new String[][]{
		{TileFactory.TILE_TYPE_BIG, "New", "/assets/placeholder.png", ""+CodeReference.CODE_NEW_PICTURE},
		{TileFactory.TILE_TYPE_BIG, "Open", "/assets/placeholder.png", ""+CodeReference.CODE_OPEN_FILE},
		{TileFactory.TILE_TYPE_BIG, "Save", "/assets/placeholder.png", ""+CodeReference.CODE_SAVE_THING},
		{TileFactory.TILE_TYPE_BIG, "Save as", "/assets/placeholder.png", ""+CodeReference.CODE_SAVE_AS},
		{TileFactory.TILE_TYPE_BIG, "Meta", "/assets/placeholder.png", ""+CodeReference.CODE_OPEN_META},
		{TileFactory.TILE_TYPE_BIG, "Exit", "/assets/placeholder.png", ""+CodeReference.CODE_EXIT},
	};
	
	public FilePage() {
		super(PAGE_NAME);
		for(String[] s : BUTTONS) {
			switch(s[0]) {
				case TileFactory.TILE_TYPE_BIG:
					addTile(TileFactory.generateTileBig(s[2], s[1], Integer.parseInt(s[3])));
					break;
				case TileFactory.TILE_TYPE_GRID:
					//TODO: Change entry data structure
					break;
				}
		}
	}

}
