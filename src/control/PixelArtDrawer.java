package control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import control.code.CodeReference;
import control.code.KeyBindings;
import control.config.DataAccess;
import manager.Manager;
import manager.pen.Pen;
import misc.Canvas;
import visual.View;
import visual.popouts.PopoutKeybindSelect;

/**
 * 
 * Key input is universal and redirected to one location (keyboard shortcuts not dependent on context (mostly))
 * 
 * @author Ada Clevinger
 *
 */

public class PixelArtDrawer implements InputHandler{

	/*
	 * Right click alt-fire second color
	 * Fill tool
	 * Select tool
	 * Replace all of one color with another
	 */
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static String TEXT_WIDTH_REQUEST = "Please provide the width of the new image.";
	private final static String TEXT_HEIGHT_REQUEST = "Please provide the height of the new image.";
	private final static String TEXT_CHOICE_PICTURE = "Picture";
	private final static String TEXT_CHOICE_ANIMATION = "Animation";

	private final static int MAX_PEN_SIZE = 128;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private View view;
	
	private Manager manager;
	
	private KeyBindings keyBind;
	
	private DataAccess dataAccess;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public PixelArtDrawer() {
		CodeReference.setup();
		manager = new Manager();
		view = new View(this);
		dataAccess = new DataAccess();
		keyBind = new KeyBindings(dataAccess.getKeyMappings());
		manager.setupPallettes(dataAccess.getColorPallettes());
		generateEmptyImage("Default", 320, 320);
	}
	
//---  Operations   ---------------------------------------------------------------------------

	//-- Receiving Code from View  ----------------------------
	
	/**
	 * active is the element that produced the code, be it a Tile in the Settings Bar or one of the
	 * Corkboards.
	 * 
	 * @param in
	 * @param active
	 */
	
	public void handleCodeInput(int in, String active) {
		Boolean happ = checkRanges(in, active);
		if(happ == null) {
			happ = checkCommands(in, active);
		}
		if(happ != null && happ) {
			System.out.println("~~~!!!REFRESH!!!~~~");
			refreshSettingsBar();
			updateCorkboard(false);
		}
	}
	
	public void handleDrawInput(int x, int y, int duration, String nom) {
		if(manager.drawToPicture(nom, x, y, duration)) {
			refreshSettingsBar();
			updateCorkboard(false);
		}
	}

	public void handleKeyInput(char code, int keyType) {
		System.out.println(keyType + " " + code);
		int val = keyBind.interpretKeyInput(code, keyType);
		handleCodeInput(val, null);
		if(val != -1) {
			handleCodeInput(CodeReference.CODE_PERFORM_REFRESH, null);
		}
	}
	
	//-- Codes from Ranges  -----------------------------------
	
	private Boolean checkRanges(int in, String active) {
		Boolean happ = checkRangeColors(in, active);
		if(happ == null) {
			happ = checkRangePenType(in, active);
		}
		if(happ == null) {
			happ = checkRangeLayers(in, active);
		}
		return happ;
	}
	
	private Boolean checkRangeColors(int in, String active) {
		if(in >= CodeReference.CODE_RANGE_SELECT_COLOR && in < CodeReference.CODE_RANGE_SELECT_DRAW_TYPE) {
			int use = (in - CodeReference.CODE_RANGE_SELECT_COLOR - manager.getPen().getCurrentPalletCodeBase());
			manager.getPen().setActiveColor(use);
			return true;
		}
		return null;
	}
	
	private Boolean checkRangePenType(int in, String active) {
		if(in >= CodeReference.CODE_RANGE_SELECT_DRAW_TYPE) {
			int use = in - CodeReference.CODE_RANGE_SELECT_DRAW_TYPE;
			manager.getPen().setPenType(use);
			return true;
		}
		return null;
	}
	
	private Boolean checkRangeLayers(int in, String active) {
		if(in >= CodeReference.CODE_RANGE_LAYER_SELECT && in < CodeReference.CODE_RANGE_SELECT_COLOR) {
			String use = view.getActiveElement();
			return true;
		}
		return null;
	}
	
	//-- Codes from Constants  --------------------------------
	
	private Boolean checkCommands(int in, String active) {
		Boolean happ = checkFileCommand(in, active);
		if(happ == null) {
			happ = checkMetaPropertyCommands(in, active);
		}
		if(happ == null) {
			happ = checkSettingsBarAutomatic(in, active);
		}
		if(happ == null) {
			happ = checkDrawingCommands(in, active);
		}
		return happ;
	}
	
		//-- Settings Bar  ------------------------------------
	
	private Boolean checkFileCommand(int in, String active) {
		switch(in) {
			case CodeReference.CODE_NEW_THING:
				makeNewThing();
				return true;
			case CodeReference.CODE_RENAME:
				String old = view.getActiveElement();
				if(old == null) {
					return false;
				}
				String newName = view.requestStringInput("Please provide the new image name");
				HashMap<String, String> mappings = manager.rename(old, newName);
				view.rename(mappings);
				return true;
			case CodeReference.CODE_CLOSE_THING:
				String thng = view.getActiveElement();
				if(thng == null) {
					return false;
				}
				removeThing(thng);
				return true;
			case CodeReference.CODE_DUPLICATE_THING:
				String dup = view.getActiveElement();
				if(dup == null) {
					return false;
				}
				view.duplicateThing(dup, manager.duplicate(dup));
				return true;
			case CodeReference.CODE_OPEN_FILE:
				loadFile();
				return true;
			case CodeReference.CODE_SAVE_THING:
				String sav = view.getActiveElement();
				if(sav == null) {
					return false;
				}
				saveThing(sav);
				return false;
			case CodeReference.CODE_SAVE_AS:
				String savA = view.getActiveElement();
				if(savA == null) {
					return false;
				}
				saveThingAs(savA);
				return false;
			case CodeReference.CODE_OPEN_META:
				//TODO: Plan out what the meta-settings menu should even do/keep track of
				return false;
			case CodeReference.CODE_EXIT:
				if(view.requestConfirmation("Are you sure you want to exit?")) {
					System.exit(0);
					if(view.requestConfirmation("Would you like to save backups of your current work?")) {
						saveEverything();
					}
				}
				return true;
			default:
				return null;
			}
	}
	
	private Boolean checkDrawingCommands(int in, String active) {
		Pen p = manager.getPen();
		switch(in) {
			case CodeReference.CODE_PEN_SIZE_INCREMENT:
				p.setPenSize(p.getPenSize() + 1);
				return true;
			case CodeReference.CODE_PEN_SIZE_DECREMENT:
				p.setPenSize(p.getPenSize() - 1);
				return true;
			case CodeReference.CODE_PEN_INCREMENT_BLEND_QUOTIENT:
				p.setBlendQuotient(p.getBlendQuotient() + .01);
				return true;
			case CodeReference.CODE_PEN_DECREMENT_BLEND_QUOTIENT:
				p.setBlendQuotient(p.getBlendQuotient() - .01);
				return true;
			case CodeReference.CODE_PEN_SIZE_SET:
				try {
					int val = Integer.parseInt(view.getTileContents(active));
					manager.getPen().setPenSize(val);
				}
				catch(Exception e) {}
				return false;
			case CodeReference.CODE_PEN_TOGGLE_BLEND:
				manager.getPen().toggleShading();
				return true;
			case CodeReference.CODE_PEN_SET_BLEND_QUOTIENT:
				try {
					int val = Integer.parseInt(view.getTileContents(active));
					manager.getPen().setBlendQuotient(((double)val) / 100.0);
				}
				catch(Exception e) {}
				return false;
			case CodeReference.CODE_COLOR_ADD:
				Color nC = view.requestColorChoice(null);
				manager.getPen().addColor(nC);
				storeCurrentPallettes();
				return true;
			case CodeReference.CODE_COLOR_EDIT:
				Color base = manager.getPen().getActiveColor();
				Color out = view.requestColorChoice(base);
				manager.getPen().editColor(manager.getPen().getActiveColorIndex(), out);
				storeCurrentPallettes();
				return true;
			case CodeReference.CODE_COLOR_REMOVE:
				manager.getPen().removeColor(manager.getPen().getActiveColorIndex());
				storeCurrentPallettes();
				return true;
			case CodeReference.CODE_PEN_PALLETTE_NEXT:
				manager.getPen().setPallet(manager.getPen().getCurrentPallet()+1);
				return true;
			case CodeReference.CODE_PEN_PALLETTE_PREV:
				manager.getPen().setPallet(manager.getPen().getCurrentPallet()-1);
				return true;
			case CodeReference.CODE_PEN_PALLETTE_NEW:
				manager.getPen().addPallet();
				manager.getPen().setPallet(manager.getPen().getNumberPallettes()-1);
				storeCurrentPallettes();
				return true;
			case CodeReference.CODE_PEN_PALLETTE_REMOVE:
				if(view.requestConfirmation("Are you sure you want to remove this Pallette?")) {
					manager.getPen().removePallet(manager.getPen().getCurrentPallet());
					if(manager.getPen().getNumberPallettes() == 0) {
						manager.getPen().addPallet();
					}
					storeCurrentPallettes();
				}
				return true;
			default:
				return null;
		}
	}
	
	private Boolean checkMetaPropertyCommands(int in, String active) {
		Boolean happ = checkLayerCommands(in, active);
		if(happ == null) {
			happ = checkResizeCommands(in, active);
		}
		if(happ != null) {
			return happ;
		}
		String use = view.getActiveElement();
		switch(in) {
			case CodeReference.CODE_TOGGLE_TOOLTIPS:
				view.toggleTooltips();
				return false;
			case CodeReference.CODE_PEN_MODE_MOVE_CANVAS:
				if(use != null) {
					view.setContentLock(use, true);
					manager.getPen().setPenMode(Pen.PEN_MODE_MOVE_CANVAS);
				}
				return true;
			case CodeReference.CODE_PEN_MODE_COLOR_PICK:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_COLOR_PICK);
				return true;
			case CodeReference.CODE_PEN_MODE_FILL:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_FILL);
				return true;
			case CodeReference.CODE_PEN_MODE_DRAW:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_DRAW);
				return true;
			case CodeReference.CODE_PEN_REGION_MODE_OUTLINE:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_REGION_SELECT);
				manager.getPen().setRegionMode(Pen.REGION_MODE_OUTLINE);
				return true;
			case CodeReference.CODE_PEN_REGION_MODE_FILL:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_REGION_SELECT);
				manager.getPen().setRegionMode(Pen.REGION_MODE_FILL);
				return true;
			case CodeReference.CODE_PEN_REGION_MODE_COPY:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_REGION_SELECT);
				manager.getPen().setRegionMode(Pen.REGION_MODE_COPY);
				return true;
			case CodeReference.CODE_PEN_REGION_MODE_PASTE:
				if(use != null) {
					view.setContentLock(use, false);
				}
				manager.getPen().setPenMode(Pen.PEN_MODE_REGION_APPLY);
				manager.getPen().setRegionMode(Pen.REGION_MODE_PASTE);
				return true;
			case CodeReference.CODE_UNDO_CHANGE:
				if(use != null)
					manager.undo(use);
				return false;
			case CodeReference.CODE_REDO_CHANGE:
				if(use != null)
					manager.redo(use);
				return false;
			case CodeReference.CODE_INCREASE_ZOOM:
				if(use != null)
					manager.increaseZoom(use);
				return true;
			case CodeReference.CODE_DECREASE_ZOOM:
				if(use != null)
					manager.decreaseZoom(use);
				return true;
			case CodeReference.CODE_EDIT_KEYBINDINGS:
				HashMap<Character, Integer> result = view.requestKeybindUpdate(CodeReference.getCodeTooltips(), keyBind.getMappings());
				if(result != null) {
					keyBind.setKeyBindings(result);
					dataAccess.saveKeyMappings(result);
				}
				return false;
			default:
				return null;
		}
	}
	
	private Boolean checkResizeCommands(int in, String active) {
		String use = view.getActiveElement();
		switch(in) {
			case CodeReference.CODE_RESIZE_CANVAS:
				return true;
			case CodeReference.CODE_INCREMENT_CANVAS_HEI:
				return true;
			case CodeReference.CODE_INCREMENT_CANVAS_WID:
				return true;
			case CodeReference.CODE_DECREMENT_CANVAS_HEI:
				return true;
			case CodeReference.CODE_DECREMENT_CANVAS_WID:
				return true;
			default:
				return null;
		}
	}
	
	private Boolean checkLayerCommands(int in, String active) {
		String use = view.getActiveElement();
		switch(in) {
			case CodeReference.CODE_ACTIVE_LAYER_UP:
				if(use != null)
					manager.moveSketchActiveLayerUp(use);
				return true;
			case CodeReference.CODE_ACTIVE_LAYER_DOWN:
				if(use != null)
					manager.moveSketchActiveLayerDown(use);
				return true;
			case CodeReference.CODE_LAYER_DISPLAY_ALL:
				if(use != null)
					manager.setSketchLayersAll(use);
				return true;
			case CodeReference.CODE_LAYER_DISPLAY_BENEATH:
				if(use != null)
					manager.setSketchLayersBeneath(use);
				return true;
			case CodeReference.CODE_LAYER_DISPLAY_ACTIVE:
				if(use != null)
					manager.setSketchLayersActive(use);
				return true;
			case CodeReference.CODE_ADD_LAYER:
				if(use != null)
					manager.addLayer(active);
				return true;
			case CodeReference.CODE_REMOVE_LAYER:
				//TODO: Make custom popout for layer selection
				if(use != null)
					manager.removeLayer(use, view.requestIntInput("Which layer (#) do you want to remove?"));
				return true;
			case CodeReference.CODE_MOVE_LAYER:
				if(use != null)
					manager.moveLayer(use, view.requestIntInput("Move Which Layer?"), view.requestIntInput("To Where?"));
				return true;
			case CodeReference.CODE_MOVE_LAYER_UP:
				if(use != null)
					manager.moveLayerUp(use);
				return true;
			case CodeReference.CODE_MOVE_LAYER_DOWN:
				if(use != null)
					manager.moveLayerDown(use);
				return true;
			default:
				return null;
		}
	}
	
	private Boolean checkSettingsBarAutomatic(int in, String active) {
		switch(in) {
			case CodeReference.CODE_PERFORM_REFRESH:
				return true;
			case CodeReference.CODE_UPDATE_COLOR:
				updateColors(active);
				return false;
			case CodeReference.CODE_UPDATE_PEN_SIZE:
				updatePenSize(active);
				return false;
			case CodeReference.CODE_UPDATE_PEN_TYPE:
				updatePenType(active);
				return false;
			case CodeReference.CODE_UPDATE_PEN_BLEND:
				updatePenBlend(active);
				return false;
			case CodeReference.CODE_UPDATE_COLOR_OPTIONS:
				updateColorOptions(active);
				return false;
			case CodeReference.CODE_UPDATE_SELECTION_MODE:
				updateSelectionMode(active);
				return false;
			default:
				return null;
		}
	}

	//-- Manager Manipulation  --------------------------------
	
		//-- Add  ---------------------------------------------
	
		//-- Settings Bar Automatic  --------------------------

	//-- Instructing Manager  ---------------------------------
	
	private void makeNewThing() {
		String choice = view.requestListChoice(new String[] {TEXT_CHOICE_PICTURE, TEXT_CHOICE_ANIMATION});
		switch(choice) {
			case TEXT_CHOICE_PICTURE:
				generateEmptyImage(manager.getNextName(), view.requestIntInput(TEXT_WIDTH_REQUEST), view.requestIntInput(TEXT_HEIGHT_REQUEST));
				break;
			case TEXT_CHOICE_ANIMATION:
				break;
		}
	}
	
	private void generateEmptyImage(String nom, int wid, int hei) {
		String skNom = manager.makeNewPicture(nom, wid, hei);
		addPicture(skNom, manager.getPictureCanvas(skNom));
	}

	private void loadFile() {
		String path = getFilePath();
		//TODO: Either folder of images with a manifest on how to process them, or custom data type that the Manager can decode
		//TODO: For now, it will assume single layer images, will expand
		String nom = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
		String skNm = manager.loadInPicture(nom, path);
		addPicture(skNm, manager.getPictureCanvas(skNm));
	}
	
		//-- Remove  ------------------------------------------
	
	private void removeThing(String nom) {
		if(view.requestConfirmation("Are you sure you want to remove: " + nom + "?")) {
			manager.removeThing(nom);
			view.removeFromDisplay(nom);
		}
	}

		//-- Save  --------------------------------------------
	
	private void saveEverything() {
		manager.saveAllBackup();
	}
	
	private void saveThing(String nom) {
		String path = manager.getDefaultFilePath(nom);
		if(path == null) {
			path = view.requestFolderPath("./", "Where do you want to save this to?");
		}
		manager.saveThing(nom, path, 1, true);
	}
	
	private void saveThingAs(String nom) {
		String path = manager.getDefaultFilePath(nom);
		path = view.requestFolderPath(path, "Where do you want to save this to?");
		String savNom = view.requestStringInput("What would you like to name the file?");
		manager.saveThing(nom, savNom, path, 1, true);
	}

	private void storeCurrentPallettes() {
		dataAccess.saveColorPallettes(manager.getPen().getAllPallettes());
	}
	
	//-- Updating View  ---------------------------------------
	
		//-- Settings Bar  ------------------------------------
	
	private void refreshSettingsBar() {
		view.refreshActivePage();
	}
	
	private void updateColors(String ref) {
		ArrayList<Color> cols = new ArrayList<Color>();
		for(Color c : manager.getPen().getColors()) {
			cols.add(c);
		}
		int[] codes = new int[cols.size()];
		int codeBase = manager.getPen().getCurrentPalletCodeBase() + CodeReference.CODE_RANGE_SELECT_COLOR;
		for(int i = 0; i < cols.size(); i++) {
			codes[i] = codeBase + i;
		}
		view.updateTileGridColors(ref, cols, codes, manager.getPen().getActiveColorIndex());
	}
	
	private void updatePenSize(String ref) {
		view.updateTileNumericSelector(ref, 1, MAX_PEN_SIZE, manager.getPen().getPenSize());
	}
	
	private void updatePenBlend(String ref) {
		view.updateTileNumericSelector(ref, 0, 100, (int)(100.0 * manager.getPen().getBlendQuotient()));
	}
	
	private void updatePenType(String ref) {
		ArrayList<String> paths = new ArrayList<String>();
		for(int i : manager.getPen().getPenDrawTypes()) {
			if(i < CodeReference.REF_DRAW_TYPE_PATHS.length) {
				paths.add(CodeReference.REF_DRAW_TYPE_PATHS[i]);
			}
		}
		int[] out = new int[paths.size()];
		for(int i = 0; i < out.length; i++) {
			out[i] = CodeReference.CODE_RANGE_SELECT_DRAW_TYPE + i;
		}
		view.updateTileGridImages(ref, paths, out, manager.getPen().getPenType());
	}
	
	private void updateColorOptions(String ref) {
		ArrayList<String> paths = new ArrayList<String>();
		int[] info = CodeReference.REF_COLOR_OPTION_CODES;
		for(int i : info) {
			paths.add(CodeReference.getCodeImagePath(i));
		}
		view.updateTileGridImages(ref, paths, CodeReference.REF_COLOR_OPTION_CODES, 0);
	}
	
	private void updateSelectionMode(String ref) {
		int[] codes = CodeReference.REF_PEN_MODE_CODES;
		ArrayList<String> paths = new ArrayList<String>();
		for(int i : codes) {
			paths.add(CodeReference.getCodeImagePath(i));
		}
		view.updateTileGridImages(ref, paths, codes, manager.getPen().getPenMode());
	}
	
		//-- Corkboard  ---------------------------------------
	
	private void updateCorkboard(boolean force) {
		for(String nom : manager.getSketchNames(force)) {
			updateThing(nom, manager.getSketchImages(nom), manager.getSketchZoom(nom));
		}
	}
	
	private void updateThing(String nom, Canvas[] imgs, int zoom) {
		view.updateDisplay(nom, imgs, zoom);
	}

	private void addAnimation(String nom, Canvas[] imgs) {
		view.addAnimation(nom, imgs);
	}
	
	private void addPicture(String nom, Canvas img) {
		view.addPicture(nom, img);
	}
	
//---  Support   ------------------------------------------------------------------------------

	private String getFilePath() {
		String path = view.requestFilePath("./", "Please select the file you want.");
		path = path.replaceAll("\\\\", "/");
		while(path.contains("//")) {
			path = path.replaceAll("//", "/");
		}
		return path;
	}
	
}
