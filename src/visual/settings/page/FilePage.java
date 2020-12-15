package visual.settings.page;

import control.CodeReference;

public class FilePage extends Page{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "File";
	
//---  Constructors   -------------------------------------------------------------------------
	
	public FilePage() {
		super(PAGE_NAME);
		addTileBig("A", null, null, "New", "/assets/placeholder.png", CodeReference.CODE_NEW_THING);
		addTileBig("B", null, null, "Duplicate", "/assets/placeholder.png", CodeReference.CODE_DUPLICATE_THING);
		addTileBig("C", null, null, "Open", "/assets/placeholder.png", CodeReference.CODE_OPEN_FILE);
		addTileBig("D", null, null, "Rename", "/assets/placeholder.png", CodeReference.CODE_RENAME);
		addTileBig("E", null, null, "Save", "/assets/placeholder.png", CodeReference.CODE_SAVE_THING);
		addTileBig("F", null, null, "Save as", "/assets/placeholder.png", CodeReference.CODE_SAVE_AS);
		addTileBig("G", null, null, "Meta", "/assets/placeholder.png", CodeReference.CODE_OPEN_META);
		addTileBig("H", null, null, "Exit", "/assets/placeholder.png", CodeReference.CODE_EXIT);
	}
	
}
