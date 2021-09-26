package control.code;

import java.util.HashMap;

import input.manager.actionevent.KeyActionEvent;

public class KeyBindings {

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Character, Integer> mappings;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public KeyBindings(HashMap<Character, Integer> startMapping) {
		mappings = startMapping;
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
	
	public void setKeyBindings(HashMap<Character, Integer> newMap) {
		mappings = newMap;
	}
	
	public void releaseKeyBinding(char key) {
		mappings.remove(key);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public HashMap<Character, Integer> getMappings(){
		return mappings;
	}
	
}
