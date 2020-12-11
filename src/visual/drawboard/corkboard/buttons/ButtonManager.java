package visual.drawboard.corkboard.buttons;

import java.util.ArrayList;

import control.CodeReference;
import visual.drawboard.corkboard.Corkboard;

public class ButtonManager {

//---  Constants   ----------------------------------------------------------------------------

	private static ButtonInformation[] DEFAULT_BUTTONS = new ButtonInformation[] {
			new ButtonInformation("lock", CodeReference.IMAGE_PATH_TOGGLE_LOCK, CodeReference.CODE_TOGGLE_LOCK_CANVAS),
			new ButtonInformation("zoomIn", CodeReference.IMAGE_PATH_ZOOM_IN, CodeReference.CODE_INCREASE_ZOOM),
			new ButtonInformation("zoomOut", CodeReference.IMAGE_PATH_ZOOM_OUT, CodeReference.CODE_DECREASE_ZOOM),
			new ButtonInformation("undo", CodeReference.IMAGE_PATH_UNDO, CodeReference.CODE_UNDO_CHANGE),
			new ButtonInformation("redo", CodeReference.IMAGE_PATH_REDO, CodeReference.CODE_REDO_CHANGE),
			new ButtonInformation("all", CodeReference.IMAGE_PATH_LAYER_ALL, CodeReference.CODE_LAYER_DISPLAY_ALL),
			new ButtonInformation("beneath", CodeReference.IMAGE_PATH_LAYER_BENEATH, CodeReference.CODE_LAYER_DISPLAY_BENEATH),
			new ButtonInformation("active", CodeReference.IMAGE_PATH_LAYER_ACTIVE, CodeReference.CODE_LAYER_DISPLAY_ACTIVE),
			new ButtonInformation("add", CodeReference.IMAGE_PATH_ADD_LAYER, CodeReference.CODE_ADD_LAYER),
			new ButtonInformation("up", CodeReference.IMAGE_PATH_ACTIVE_LAYER_UP, CodeReference.CODE_ACTIVE_LAYER_UP),
			new ButtonInformation("down", CodeReference.IMAGE_PATH_ACTIVE_LAYER_DOWN, CodeReference.CODE_ACTIVE_LAYER_DOWN),
	};

//---  Instance Variables   -------------------------------------------------------------------
	
	private static ArrayList<ButtonInformation> defaultButtons;

//---  Operations   ---------------------------------------------------------------------------
	
	public static void fillButtons(Corkboard given) {
		ensureSetup();
		given.assignButtonInformation(defaultButtons);
	}
	
	public static void linkCorkboard(Corkboard in) {
		ensureSetup();
		fillButtons(in);
	}
	
	public static void unlinkCorkboard(Corkboard in) {
		ensureSetup();
		ArrayList<ButtonInformation> use = new ArrayList<ButtonInformation>();
		use.addAll(defaultButtons);
		in.assignButtonInformation(use);
	}
	
	private static void ensureSetup() {
		if(defaultButtons != null) {
			return;
		}
		defaultButtons = new ArrayList<ButtonInformation>();
		for(ButtonInformation bI : DEFAULT_BUTTONS) {
			defaultButtons.add(bI);
		}
	}
	
}
