package visual.settings.page;

import control.CodeReference;

public class DrawingPage extends Page {

	private final static String PAGE_NAME = "Drawing";
	
	public DrawingPage() {
		super(PAGE_NAME);
		addTileGrid("pens", new String[] {"/assets/placeholder.png", "/assets/placeholder.png","/assets/placeholder.png","/assets/placeholder.png",}, "lab", new int[] {0, 1, 2, 3}, 3);
		addTileColorGrid(CodeReference.REF_COLOR_GRID, "Colors", 3);
	}

}
