package visual.drawboard.corkboard.buttons;

import java.util.ArrayList;

import control.code.CodeInfo;
import control.code.CodeReference;
import visual.drawboard.corkboard.Corkboard;

public class ButtonManager {

//---  Constants   ----------------------------------------------------------------------------

	private static int[] DEFAULT_BUTTONS = new int[] {
		CodeReference.CODE_PEN_MODE_MOVE_CANVAS,
		CodeReference.CODE_INCREASE_ZOOM,
		CodeReference.CODE_DECREASE_ZOOM,
		CodeReference.CODE_UNDO_CHANGE,
		CodeReference.CODE_REDO_CHANGE,
		CodeReference.CODE_LAYER_DISPLAY_ALL,
		CodeReference.CODE_LAYER_DISPLAY_BENEATH,
		CodeReference.CODE_LAYER_DISPLAY_ACTIVE,
		CodeReference.CODE_ADD_LAYER,
		CodeReference.CODE_ACTIVE_LAYER_UP,
		CodeReference.CODE_ACTIVE_LAYER_DOWN,
		CodeReference.CODE_MOVE_LAYER_UP,
		CodeReference.CODE_MOVE_LAYER_DOWN,
	};

//---  Instance Variables   -------------------------------------------------------------------
	
	private static ArrayList<CodeInfo> defaultButtons;

//---  Operations   ---------------------------------------------------------------------------
	
	public static void fillButtons(Corkboard given) {
		ensureSetup();
		given.assignCodeInfo(defaultButtons);
	}
	
	public static void linkCorkboard(Corkboard in) {
		ensureSetup();
		fillButtons(in);
	}
	
	public static void unlinkCorkboard(Corkboard in) {
		ensureSetup();
		ArrayList<CodeInfo> use = new ArrayList<CodeInfo>();
		use.addAll(defaultButtons);
		in.assignCodeInfo(use);
	}
	
	private static void ensureSetup() {
		if(defaultButtons != null) {
			return;
		}
		defaultButtons = new ArrayList<CodeInfo>();
		for(int bI : DEFAULT_BUTTONS) {
			defaultButtons.add(CodeReference.getCodeInfo(bI));
		}
	}
	
}
