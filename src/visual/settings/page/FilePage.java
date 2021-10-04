package visual.settings.page;

import control.code.CodeReference;

public class FilePage extends Page{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "File";
	
//---  Constructors   -------------------------------------------------------------------------
	
	public FilePage() {
		super(PAGE_NAME);
		addTileBig("A", null, null, "New", CodeReference.CODE_NEW_THING);
		addTileBig("B", null, null, "Duplicate", CodeReference.CODE_DUPLICATE_THING);
		addTileBig("C", null, null, "Open", CodeReference.CODE_OPEN_FILE);
		addTileBig("D", null, null, "Rename", CodeReference.CODE_RENAME);
		addTileBig("E", null, null, "Save", CodeReference.CODE_SAVE_THING);
		addTileBig("F", null, null, "Save as", CodeReference.CODE_SAVE_AS);
		//addTileBig("G", null, null, "Meta", CodeReference.CODE_OPEN_META);
		addTileBig("I", null, null, "Keybindings", CodeReference.CODE_EDIT_KEYBINDINGS);
		addTileBig("H", null, null, "Exit", CodeReference.CODE_EXIT);
	}
	
}
