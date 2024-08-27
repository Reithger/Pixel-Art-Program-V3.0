package visual.settings.page;

import java.util.ArrayList;

import control.code.CodeReference;
import visual.CodeMetaAccess;

public class PageFactory {

	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * This function generates an ArrayList of Page subclasses that specialize into
	 * each type of menu for the user to interact with (File options, Drawing options, and
	 * a Settings page eventually).
	 * 
	 * @return - ArrayList of Page objects
	 */
	
	public static ArrayList<Page> generateStartingPages(){
		ArrayList<Page> out = new ArrayList<Page>();
		out.add(generateFilePage());
		out.add(generateDrawingPage());
		//out.add(generateSettingsPage());
		return out;
	}
	
	public static void assignCodeReference(CodeMetaAccess cma) {
		Page.assignCodeReference(cma);
	}
	
	private static Page generateFilePage() {
		Page p = new Page("File");
		
		p.addTileBig("A", null, null, "New", CodeReference.CODE_NEW_THING);
		p.addTileBig("B", null, null, "Duplicate", CodeReference.CODE_DUPLICATE_THING);
		p.addTileBig("C", null, null, "Open", CodeReference.CODE_OPEN_FILE);
		p.addTileBig("D", null, null, "Rename", CodeReference.CODE_RENAME);
		p.addTileBig("E", null, null, "Save", CodeReference.CODE_SAVE_THING);
		p.addTileBig("F", null, null, "Save as", CodeReference.CODE_SAVE_AS);
		//addTileBig("G", null, null, "Meta", CodeReference.CODE_OPEN_META);
		p.addTileBig("I", null, null, "Keybindings", CodeReference.CODE_EDIT_KEYBINDINGS);
		p.addTileBig("H", null, null, "Exit", CodeReference.CODE_EXIT);
		
		return p;
	}
	
	/**
	 * 
	 * Generating function for the DrawingPage instance of Page.
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
	 * 
	 * All of that setup logic has been moved into the PageFactory class to remove the dependency on CodeReference, which
	 * now puts into question the purpose of this class as anything but a collection of references to another class. Hmm.
	 * 
	 */
	
	private static Page generateDrawingPage() {
		Page p = new Page("Drawing");
		
		p.addTileGrid(CodeReference.REF_SELECTION_MODE, CodeReference.CODE_UPDATE_SELECTION_MODE, null, "Mode", 2, true);
		p.addTileGrid(CodeReference.REF_PEN_TYPE_GRID, CodeReference.CODE_UPDATE_PEN_TYPE, null, "Pen Types", 3, true);
		p.addTileGrid(CodeReference.REF_COLOR_GRID, CodeReference.CODE_UPDATE_COLOR, null, "Colors", 3, true);
		p.addTileGrid(CodeReference.REF_COLOR_OPTIONS, CodeReference.CODE_UPDATE_COLOR_OPTIONS, null, "Edit Colors", 2, false);
		p.addTileNumericSelector(CodeReference.REF_PEN_SIZE, CodeReference.CODE_UPDATE_PEN_SIZE, CodeReference.CODE_PEN_SIZE_SET,
				"Pen Size", 1, 32, CodeReference.CODE_PEN_SIZE_DECREMENT, CodeReference.CODE_PEN_SIZE_INCREMENT, CodeReference.CODE_PEN_SIZE_SET);
		p.addTileNumericSelector(CodeReference.REF_PEN_BLEND, CodeReference.CODE_UPDATE_PEN_BLEND, CodeReference.CODE_PEN_SET_BLEND_QUOTIENT,
				"Blend Quotient", 1, 32, CodeReference.CODE_PEN_DECREMENT_BLEND_QUOTIENT, CodeReference.CODE_PEN_INCREMENT_BLEND_QUOTIENT, 
				CodeReference.CODE_PEN_SET_BLEND_QUOTIENT);
		
		return p;
	}
	
	private static Page generateSettingsPage() {
		Page p = new Page("Meta Properties");
		
		return p;
	}
	
	
}
