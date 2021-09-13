package visual.settings.page;

import control.code.CodeReference;

public class DrawToolsPage extends Page {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "Drawing";
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawToolsPage() {
		super(PAGE_NAME);
		addTileGrid(CodeReference.REF_SELECTION_MODE, CodeReference.CODE_UPDATE_SELECTION_MODE, null, "Mode", 2, false);
		addTileGrid(CodeReference.REF_PEN_TYPE_GRID, CodeReference.CODE_UPDATE_PEN_TYPE, null, "Pen Types", 3, true);
		addTileGrid(CodeReference.REF_COLOR_GRID, CodeReference.CODE_UPDATE_COLOR, null, "Colors", 3, true);
		addTileGrid(CodeReference.REF_COLOR_OPTIONS, CodeReference.CODE_UPDATE_COLOR_OPTIONS, null, "Edit Colors", 2, false);
		addTileNumericSelector(CodeReference.REF_PEN_SIZE, CodeReference.CODE_UPDATE_PEN_SIZE, CodeReference.CODE_PEN_SIZE_SET, "Pen Size", 1, 32, CodeReference.CODE_PEN_SIZE_DECREMENT, CodeReference.CODE_PEN_SIZE_INCREMENT, CodeReference.CODE_PEN_SIZE_SET);
		addTileNumericSelector(CodeReference.REF_PEN_BLEND, CodeReference.CODE_UPDATE_PEN_BLEND, CodeReference.CODE_PEN_SET_BLEND_QUOTIENT, "Blend Quotient", 1, 32, CodeReference.CODE_PEN_DECREMENT_BLEND_QUOTIENT, CodeReference.CODE_PEN_INCREMENT_BLEND_QUOTIENT, CodeReference.CODE_PEN_SET_BLEND_QUOTIENT);
	}
	
}
