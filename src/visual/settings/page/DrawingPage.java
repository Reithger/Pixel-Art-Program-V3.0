package visual.settings.page;

import control.CodeReference;

public class DrawingPage extends Page {

	private final static String PAGE_NAME = "Drawing";
	
	public DrawingPage() {
		super(PAGE_NAME);
		addTileGrid("pens", new String[] {"/assets/placeholder.png", "/assets/placeholder.png","/assets/placeholder.png","/assets/placeholder.png",}, "lab", new int[] {0, 1, 2, 3}, 3);
		addTileColorGrid(CodeReference.REF_COLOR_GRID, "Colors", 3);
		addTileNumericSelector(CodeReference.REF_PEN_SIZE, "Pen Size", 1, 32, CodeReference.CODE_PEN_SIZE_DECREMENT, CodeReference.CODE_PEN_SIZE_INCREMENT, CodeReference.CODE_PEN_SIZE_SET);
	}
	
	public void refresh() {
		passCodeInput(CodeReference.CODE_UPDATE_COLOR, CodeReference.REF_COLOR_GRID);
		passCodeInput(CodeReference.CODE_UPDATE_PEN_SIZE, CodeReference.REF_PEN_SIZE);
	
	}

}
