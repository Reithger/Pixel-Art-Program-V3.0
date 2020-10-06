package settings.pages;

import settings.pages.tile.TileFactory;

public class FilePage extends Page{

	private final static String PAGE_NAME = "File";
	
	public FilePage() {
		super(PAGE_NAME);
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
		addTile(TileFactory.generateTileBig("/assets/placeholder.png", "Test", 15));
	}

	@Override
	public void processInput(int code) {
		// TODO Auto-generated method stub
		
	}

}
