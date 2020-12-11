package control;

import java.util.HashMap;

public class KeyBindings {

//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Character, Integer> mappings;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public KeyBindings() {
		mappings = new HashMap<Character, Integer>();
		setKeyBinding('k', CodeReference.CODE_PEN_SIZE_DECREMENT);
		setKeyBinding('l', CodeReference.CODE_PEN_SIZE_INCREMENT);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public int interpretKeyInput(char in) {
		Integer out = mappings.get(in);
		if(out == null) {
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
