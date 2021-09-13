package visual.settings.page;

import control.code.CodeReference;

public class FilePage extends Page{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "File";
	
//---  Constructors   -------------------------------------------------------------------------
	
	public FilePage() {
		super(PAGE_NAME);
		addTileBig("A", null, null, "New", CodeReference.getCodeImagePath(CodeReference.CODE_NEW_THING), CodeReference.CODE_NEW_THING);
		addTileBig("B", null, null, "Duplicate", CodeReference.getCodeImagePath(CodeReference.CODE_DUPLICATE_THING), CodeReference.CODE_DUPLICATE_THING);
		addTileBig("C", null, null, "Open", CodeReference.getCodeImagePath(CodeReference.CODE_OPEN_FILE), CodeReference.CODE_OPEN_FILE);
		addTileBig("D", null, null, "Rename", CodeReference.getCodeImagePath(CodeReference.CODE_RENAME), CodeReference.CODE_RENAME);
		addTileBig("E", null, null, "Save", CodeReference.getCodeImagePath(CodeReference.CODE_SAVE_THING), CodeReference.CODE_SAVE_THING);
		addTileBig("F", null, null, "Save as", CodeReference.getCodeImagePath(CodeReference.CODE_SAVE_AS), CodeReference.CODE_SAVE_AS);
		addTileBig("G", null, null, "Meta", CodeReference.getCodeImagePath(CodeReference.CODE_OPEN_META), CodeReference.CODE_OPEN_META);
		addTileBig("H", null, null, "Exit", CodeReference.getCodeImagePath(CodeReference.CODE_EXIT), CodeReference.CODE_EXIT);
	}
	
}
