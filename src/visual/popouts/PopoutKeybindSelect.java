package visual.popouts;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import visual.composite.HandlePanel;
import visual.composite.popout.PopoutWindow;

public class PopoutKeybindSelect extends PopoutWindow{
	
//---  Constants   ----------------------------------------------------------------------------

	private final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 16);
	private final static String GROUP_SCROLL = "scroll";
	private final static int SCROLL_MULTIPLIER = -5;
	private final static Color BACKGROUND_COLOR = new Color(190, 190, 190);
	private final static int TOP_BUFFER_RATIO = 15;
	private final static int CODE_SUBMIT = 9999;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<Integer, String> describeCodes;
	private HashMap<Character, Integer> codeMappings;
	private HashMap<Integer, HashSet<Character>> reverseMapping;
	private HandlePanel p;
	
	private volatile boolean ready;
	
	private int active;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public PopoutKeybindSelect(int width, int height, HashMap<Integer, String> codeDescription, HashMap<Character, Integer> currentMappings) {
		super(width, height);
		p = getHandlePanel();
		ready = false;
		active = -1;
		describeCodes = codeDescription;
		codeMappings = new HashMap<Character, Integer>();
		reverseMapping = new HashMap<Integer, HashSet<Character>>();
		for(Character c : currentMappings.keySet()) {
			int code = currentMappings.get(c);
			codeMappings.put(c, currentMappings.get(c));
			if(reverseMapping.get(code) == null) {
				reverseMapping.put(code, new HashSet<Character>());
			}
			reverseMapping.get(code).add(c);
		}
		
		getHandlePanel().getPanel().setBackground(BACKGROUND_COLOR);
		drawPage();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	private void drawPage() {
		p.removeAllElements();
		int height = getHandlePanel().getTextHeight(DEFAULT_FONT);
		
		int totWidth = getWidth();
		int startY = getHeight() / TOP_BUFFER_RATIO;
		int totHeight = getHeight() - startY;
		
		int currX = 0;
		int currY = startY;
		
		int size = describeCodes.keySet().size();
		int depth = totHeight;
		
		if(size * height > totHeight * 2) {
			depth = currY + size * height / 2 + height;
		}
		
		handleRectangle("blocking_rect", "no_move", 20, getWidth() / 2, startY / 2, getWidth(), startY, BACKGROUND_COLOR, Color.black);

		this.handleTextButton("apply_changes", "no_move", 30, getWidth() / 2, startY / 2, getWidth() / 5, startY * 4 / 5, DEFAULT_FONT, "Apply Changes", CODE_SUBMIT, Color.white, Color.black);
		
		for(int i : describeCodes.keySet()) {
			composeLine(i, currX, currY, totWidth / 2, height);
			currY += height;
			if(currY + height >= depth) {
				currX += totWidth / 2;
				currY = startY;
			}
		}
		if(depth != totHeight) {
			p.addScrollbar("scrollbar", 15, "no frame", totWidth - totWidth / 35, startY, totWidth / 35, totHeight, startY, totHeight, GROUP_SCROLL, true);
		}
	}
	
	private void updateMappings() {
		reverseMapping = new HashMap<Integer, HashSet<Character>>();
		for(Character c : codeMappings.keySet()) {
			int code = codeMappings.get(c);
			if(reverseMapping.get(code) == null) {
				reverseMapping.put(code, new HashSet<Character>());
			}
			reverseMapping.get(code).add(c);
		}
		drawPage();
	}
	
	private void composeLine(int code, int x, int y, int width, int height) {
		String descript = describeCodes.get(code);
		String[] currMap = interpret(code);
		handleText("code_" + code + "_description", "scroll", 10, x + width / 4, y + height / 2, width / 2, height, DEFAULT_FONT, descript);
		handleText("code_" + code + "_values", "scroll", 10, x + 3 * width / 4, y + height / 2, width / 2, height, DEFAULT_FONT, Arrays.toString(currMap));
		handleLine("code_" + code + "_separator", "scroll", 10, x + width / 2, y + 3, x + width / 2, y + height - 3, 1, code == active ? Color.green : Color.black);
		handleButton("code_" + code + "_button", "scroll", 15, x + 3 * width / 4, y + height / 2, width / 2, height, code);
	}
	
	private String[] interpret(int code) {
		HashSet<Character> reverse = reverseMapping.get(code);
		if(reverse == null) {
			return new String[1];
		}
		String[] convert = new String[reverse.size()];
		int post = 0;
		for(Character c : reverse) {
			int val = (int)c;
			if(val <= 26 && val > 0) {
				convert[post++] = "Ctrl " + (char)(val + 64);
			}
			else {
				convert[post++] = c+"";
			}
		}
		return convert;
	}

//---  Input Methods   ------------------------------------------------------------------------
	
	@Override
	public void clickAction(int code, int x, int y) {
		if(code == CODE_SUBMIT) {
			ready = true;
		}
		else {
			active = code;
			drawPage();
		}
	}
	
	@Override
	public void scrollAction(int scroll) {
		int newOffset = p.getOffsetY(GROUP_SCROLL) + scroll * SCROLL_MULTIPLIER;
		newOffset = newOffset > 0 ? 0 : newOffset;
		int totHeight = getHeight() - getHeight() / TOP_BUFFER_RATIO;
		int groupHeight = (describeCodes.keySet().size() / 2 + 1) * p.getTextHeight(DEFAULT_FONT);
		int max = totHeight - groupHeight;
		newOffset = newOffset < max ? max : newOffset;
		p.setOffsetY(GROUP_SCROLL, newOffset);
		drawPage();
	}
	
	@Override
	public void keyAction(char key) {
		if(active != -1) {
			codeMappings.put(key, active);
			active = -1;
			updateMappings();
			return;
		}
		switch(key) {
			case 'w':
				scrollAction(1);
				break;
			case 's':
				scrollAction(-1);
				break;
			default:
				break;
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public HashMap<Character, Integer> getResult(){
		while(!ready) {
		}
		return codeMappings;
	}

}
