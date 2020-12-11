package visual.settings.page;

import control.CodeReference;

public class FilePage extends Page{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "File";
	
//---  Constructors   -------------------------------------------------------------------------
	
	public FilePage() {
		super(PAGE_NAME);
		addTileBig("A", "New", "/assets/placeholder.png", CodeReference.CODE_NEW_THING);
		addTileBig("B", "Duplicate", "/assets/placeholder.png", CodeReference.CODE_DUPLICATE_THING);
		addTileBig("C", "Open", "/assets/placeholder.png", CodeReference.CODE_OPEN_FILE);
		addTileBig("D", "Rename", "/assets/placeholder.png", CodeReference.CODE_RENAME);
		addTileBig("E", "Save", "/assets/placeholder.png", CodeReference.CODE_SAVE_THING);
		addTileBig("F", "Save as", "/assets/placeholder.png", CodeReference.CODE_SAVE_AS);
		addTileBig("G", "Meta", "/assets/placeholder.png", CodeReference.CODE_OPEN_META);
		addTileBig("H", "Exit", "/assets/placeholder.png", CodeReference.CODE_EXIT);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	protected void refreshLocal(boolean pushUpdate) {
		
	}

}
