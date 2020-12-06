package visual.settings.page;

import control.CodeReference;

public class DrawingPage extends Page {

	private final static String PAGE_NAME = "Drawing";
	
	public DrawingPage() {
		super(PAGE_NAME);
		addTileGrid(CodeReference.REF_PEN_TYPE_GRID, "Pen Types", 3);
		addTileGrid(CodeReference.REF_COLOR_GRID, "Colors", 3);
		addTileNumericSelector(CodeReference.REF_PEN_SIZE, "Pen Size", 1, 32, CodeReference.CODE_PEN_SIZE_DECREMENT, CodeReference.CODE_PEN_SIZE_INCREMENT, CodeReference.CODE_PEN_SIZE_SET);
	}
	
	public void refresh() {
		handleCodeInput(CodeReference.CODE_UPDATE_COLOR, CodeReference.REF_COLOR_GRID);
		handleCodeInput(CodeReference.CODE_UPDATE_PEN_SIZE, CodeReference.REF_PEN_SIZE);
		handleCodeInput(CodeReference.CODE_UPDATE_PEN_TYPE, CodeReference.REF_PEN_TYPE_GRID);
	}

}
