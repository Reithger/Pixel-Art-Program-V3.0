package control;

import java.util.HashMap;

import input.manager.actionevent.KeyActionEvent;

public class KeyBindings {

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Character, Integer> mappings;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public KeyBindings() {
		mappings = new HashMap<Character, Integer>();
		setKeyBinding('k', CodeReference.CODE_PEN_SIZE_DECREMENT);
		setKeyBinding('l', CodeReference.CODE_PEN_SIZE_INCREMENT);
		for(int i = 1; i <= 10; i++) {
			setKeyBinding((""+(i%10)).charAt(0), CodeReference.CODE_RANGE_SELECT_COLOR + (i - 1));
		}
		setKeyBinding((char)25, CodeReference.CODE_REDO_CHANGE);
		setKeyBinding((char)26, CodeReference.CODE_UNDO_CHANGE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public int interpretKeyInput(char in, int keyType) {
		Integer out = mappings.get(in);
		if(out == null || keyType != KeyActionEvent.EVENT_KEY_DOWN) {
			return -1;
		}
		return out;
	}
	
	public void setKeyBinding(char key, int code) {
		mappings.put(key, code);
	}
	
	public void releaseKeyBinding(char key) {
		mappings.remove(key);
	}
	
}
