package visual.settings.page;

import control.CodeReference;

public class FilePage extends Page{

	private final static String PAGE_NAME = "File";
	
	public FilePage() {
		super(PAGE_NAME);
		addTileBig("New", "/assets/placeholder.png", CodeReference.CODE_NEW_THING);
		addTileBig("Duplicate", "/assets/placeholder.png", CodeReference.CODE_DUPLICATE_THING);
		addTileBig("Open", "/assets/placeholder.png", CodeReference.CODE_OPEN_FILE);
		addTileBig("Rename", "/assets/placeholder.png", CodeReference.CODE_RENAME);
		addTileBig("Save", "/assets/placeholder.png", CodeReference.CODE_SAVE_THING);
		addTileBig("Save as", "/assets/placeholder.png", CodeReference.CODE_SAVE_AS);
		addTileBig("Meta", "/assets/placeholder.png", CodeReference.CODE_OPEN_META);
		addTileBig("Exit", "/assets/placeholder.png", CodeReference.CODE_EXIT);
	}

}
