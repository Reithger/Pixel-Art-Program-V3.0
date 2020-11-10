package visual.settings.page;

import visual.settings.page.tile.TileFactory;

public class FilePage extends Page{

	private final static String PAGE_NAME = "File";
	private final static int CODE_NEW_THING = 1;
	private final static int CODE_OPEN_FILE = 2;
	private final static int CODE_SAVE_THING = 3;
	private final static int CODE_SAVE_AS = 4;
	private final static int CODE_OPEN_META = 5;
	private final static int CODE_EXIT = 6;
	private final static String[][] BUTTONS = new String[][]{
		{TileFactory.TILE_TYPE_BIG, "New", "/assets/placeholder.png", ""+CODE_NEW_THING},
		{TileFactory.TILE_TYPE_BIG, "Open", "/assets/placeholder.png", ""+CODE_OPEN_FILE},
		{TileFactory.TILE_TYPE_BIG, "Save", "/assets/placeholder.png", ""+CODE_SAVE_THING},
		{TileFactory.TILE_TYPE_BIG, "Save as", "/assets/placeholder.png", ""+CODE_SAVE_AS},
		{TileFactory.TILE_TYPE_BIG, "Meta", "/assets/placeholder.png", ""+CODE_OPEN_META},
		{TileFactory.TILE_TYPE_BIG, "Exit", "/assets/placeholder.png", ""+CODE_EXIT},
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

	@Override
	public void processInput(int code) {
		switch(code) {
			case CODE_NEW_THING:
				
		}
	}

}
