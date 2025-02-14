package control.code;

import java.util.HashMap;

import input.manager.actionevent.KeyActionEvent;

public class KeyBindings {

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Character, Integer> mappings;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public KeyBindings(HashMap<Character, Integer> startMapping) {
		if(startMapping != null) {
			mappings = startMapping;
		}
		else {
			defaultConstruction();
		}
	}
	
	private void defaultConstruction() {
		mappings = new HashMap<Character, Integer>();
		softSetKeyBinding('k', CodeReference.CODE_PEN_SIZE_DECREMENT);
		softSetKeyBinding('l', CodeReference.CODE_PEN_SIZE_INCREMENT);
		for(int i = 1; i <= 10; i++) {
			softSetKeyBinding((""+(i%10)).charAt(0), CodeReference.CODE_RANGE_SELECT_COLOR + (i - 1));
		}
		softSetKeyBinding((char)25, CodeReference.CODE_REDO_CHANGE);
		softSetKeyBinding((char)26, CodeReference.CODE_UNDO_CHANGE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public int interpretKeyInput(char in, int keyType) {
		Integer out = mappings.get(in);
		if(out == null || keyType != KeyActionEvent.EVENT_KEY_DOWN) {
			return -1;
		}
		return out;
	}
	
	/**
	 * 
	 * Wrapper function around setKeyBinding that handles the Exception case automatically
	 * 
	 * @param key
	 * @param code
	 */
	
	public void softSetKeyBinding(char key, int code) {
		try {
			setKeyBinding(key, code);
		}
		catch(Exception e) {
			return;
		}
	}
	
	public void setKeyBinding(char key, int code) throws Exception{
		if(mappings.containsKey(key)) {
			throw new Exception("Error: Attempt to duplicate set key binding");
		}
		mappings.put(key, code);
	}
	
	public void setKeyBindings(HashMap<Character, Integer> newMap) {
		mappings = newMap;
	}
	
	public void releaseKeyBinding(char key) {
		mappings.remove(key);
	}
	
	public void releaseKeyBindings() {
		mappings.clear();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public HashMap<Character, Integer> getMappings(){
		return mappings;
	}
	
}
