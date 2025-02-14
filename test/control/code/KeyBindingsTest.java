package control.code;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import input.manager.actionevent.KeyActionEvent;

import org.junit.jupiter.api.BeforeEach;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyBindingsTest {

	private static final int KEY_DOWN = KeyActionEvent.EVENT_KEY_DOWN;
	private KeyBindings keyBind;
	
	@BeforeEach
	void setUp() {
		HashMap<Character, Integer> defaultMap = new HashMap<Character, Integer>();
		defaultMap.put('a', 15);
		defaultMap.put('b', 20);
		defaultMap.put('c', 25);
		keyBind = new KeyBindings(defaultMap);
	}
	
	@Test
	void testConstructDefault() {
		keyBind = new KeyBindings(null);
		assertEquals(keyBind.interpretKeyInput('k', KEY_DOWN), CodeReference.CODE_PEN_SIZE_DECREMENT);
		assertEquals(keyBind.interpretKeyInput((char)25, KEY_DOWN), CodeReference.CODE_REDO_CHANGE);
		//TODO: Change defaultConstruction to use object definition we can confirm with in here
	}
	
	@Test
	void testInterpretKeyInput() {
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN), 15);
		assertEquals(keyBind.interpretKeyInput('b', KEY_DOWN), 20);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), 25);
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN + 5), -1);
		assertEquals(keyBind.interpretKeyInput('d', KEY_DOWN + 5), -1);
	}
	
	@Test
	void testSetKeyBinding() {
		keyBind.softSetKeyBinding('d', 30);
		keyBind.softSetKeyBinding('c', 35);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), 25);
		assertEquals(keyBind.interpretKeyInput('d', KEY_DOWN), 30);
		assertThrows(Exception.class, () -> {keyBind.setKeyBinding('d', 40);});
	}
	
	@Test
	void testSetKeyBindings() {
		HashMap<Character, Integer> newMap = new HashMap<Character, Integer>();
		newMap.put('d', 35);
		newMap.put('e', 40);
		newMap.put('f', 45);
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN), 15);
		assertEquals(keyBind.interpretKeyInput('b', KEY_DOWN), 20);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), 25);
		keyBind.setKeyBindings(newMap);
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN), -1);
		assertEquals(keyBind.interpretKeyInput('b', KEY_DOWN), -1);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), -1);
		assertEquals(keyBind.interpretKeyInput('d', KEY_DOWN), 35);
		assertEquals(keyBind.interpretKeyInput('e', KEY_DOWN), 40);
		assertEquals(keyBind.interpretKeyInput('f', KEY_DOWN), 45);
	}
	
	@Test
	void testReleaseKeyBinding() {
		keyBind.releaseKeyBinding('c');
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), -1);
		keyBind.softSetKeyBinding('c', 35);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), 35);
		keyBind.releaseKeyBinding('c');
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), -1);
	}
	
	@Test
	void testReleaseKeyBindings() {
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN), 15);
		assertEquals(keyBind.interpretKeyInput('b', KEY_DOWN), 20);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), 25);
		keyBind.releaseKeyBindings();
		assertEquals(keyBind.interpretKeyInput('a', KEY_DOWN), -1);
		assertEquals(keyBind.interpretKeyInput('b', KEY_DOWN), -1);
		assertEquals(keyBind.interpretKeyInput('c', KEY_DOWN), -1);
	}
	
	@Test
	void testGetMappings() {
		HashMap<Character, Integer> map = keyBind.getMappings();
		assertEquals(map.get('a'), 15);
		assertEquals(map.get('b'), 20);
		assertEquals(map.get('c'), 25);
		assertEquals(map.size(), 3);
	}

}
