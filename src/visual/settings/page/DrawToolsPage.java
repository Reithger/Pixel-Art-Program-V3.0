package visual.settings.page;

import control.code.CodeReference;

public class DrawToolsPage extends Page {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String PAGE_NAME = "Drawing";
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * Constructor for DrawToolsPage subclass of Page.
	 * 
	 * This class just exists as a distinctive object to define what menu options should exist
	 * on the Draw page of the program.
	 * 
	 * It sets up each region with a type (TileGrid: grid of buttons with distinct images/colors that prompt
	 * certain code values to change the program state, TileNumericSelector: value slider to change specific variable),
	 * a label, what code value to listen for to update what it draws, what code value to produce when it needs to prompt
	 * a redraw elsewhere, how many rows for the TileGrid (if relevant), and whether to show the currently selected option
	 * from the TileGrid choices.
	 * 
	 * This does not populate the code values/images, just sets up an environment to contain Tiles. SettingsBar, which holds
	 * the pages, instructs them on how to populate themselves. The Page abstract class does facilitate the transfer of
	 * information from SettingsBar through this DrawToolsPage to the appropriate TileGrid that is updated to show the
	 * desired selection of Grid objects.
	 * 
	 */
	
	public DrawToolsPage() {
		super(PAGE_NAME);
		addTileGrid(CodeReference.REF_SELECTION_MODE, CodeReference.CODE_UPDATE_SELECTION_MODE, null, "Mode", 2, true);
		addTileGrid(CodeReference.REF_PEN_TYPE_GRID, CodeReference.CODE_UPDATE_PEN_TYPE, null, "Pen Types", 3, true);
		addTileGrid(CodeReference.REF_COLOR_GRID, CodeReference.CODE_UPDATE_COLOR, null, "Colors", 3, true);
		addTileGrid(CodeReference.REF_COLOR_OPTIONS, CodeReference.CODE_UPDATE_COLOR_OPTIONS, null, "Edit Colors", 2, false);
		addTileNumericSelector(CodeReference.REF_PEN_SIZE, CodeReference.CODE_UPDATE_PEN_SIZE, CodeReference.CODE_PEN_SIZE_SET,
				"Pen Size", 1, 32, CodeReference.CODE_PEN_SIZE_DECREMENT, CodeReference.CODE_PEN_SIZE_INCREMENT, CodeReference.CODE_PEN_SIZE_SET);
		addTileNumericSelector(CodeReference.REF_PEN_BLEND, CodeReference.CODE_UPDATE_PEN_BLEND, CodeReference.CODE_PEN_SET_BLEND_QUOTIENT,
				"Blend Quotient", 1, 32, CodeReference.CODE_PEN_DECREMENT_BLEND_QUOTIENT, CodeReference.CODE_PEN_INCREMENT_BLEND_QUOTIENT, 
				CodeReference.CODE_PEN_SET_BLEND_QUOTIENT);
	}
	
}
