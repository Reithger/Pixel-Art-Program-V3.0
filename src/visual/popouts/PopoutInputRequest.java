package visual.popouts;

import java.awt.Color;

import input.CustomEventReceiver;
import visual.composite.popout.PopoutWindow;

/**
 * 
 * TODO: Make a way to cancel out inputs being eaten at a lower level so that I can interpret an Enter key press here
 * 
 * @author Ada Clevinger
 *
 */

public class PopoutInputRequest extends PopoutWindow{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int POPUP_WIDTH = 300;
	private final static int POPUP_HEIGHT = 200;
	private final static int CODE_SUBMIT = 5;
	private final static String ELEMENT_NAME_ENTRY = "entry";
	private final static char KEY_ENTER = (char)10;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private volatile boolean ready;
	private String[] out;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public PopoutInputRequest(String ... text) {
		super(POPUP_WIDTH, POPUP_HEIGHT);
		
		out = new String[text.length];
		
		ready = false;

		int posX = POPUP_WIDTH / (text.length + 1);
		int posY = POPUP_HEIGHT / 6;
		int labelWidth = POPUP_WIDTH * 3 / (2 * (text.length + 1));
		int labelHeight = POPUP_HEIGHT / 3;
		
		for(int i = 0; i < text.length; i++){
			String s = text[i];
			this.handleText("tex_" + i, "move", 15, posX, posY, labelWidth, labelHeight, null, s);
			posX += (POPUP_WIDTH / (text.length + 1));
		}
		
		posX = POPUP_WIDTH / (text.length + 1);
		posY += POPUP_HEIGHT / 3;
		int entryWidth = POPUP_WIDTH / (text.length + 2);
		int entryHeight = POPUP_HEIGHT / 5;
		
		for(int i = 0; i < text.length; i++){
			this.handleTextEntry(ELEMENT_NAME_ENTRY + "_" + i, "move", 15, posX, posY, entryWidth, entryHeight, -55 - i, null, "");
			this.handleRectangle("rect_" + i, "move", 5, posX, posY, entryWidth, entryHeight, Color.white, Color.black);
			posX += (POPUP_WIDTH / (text.length + 1));
		}
		getHandlePanel().setFocusElement(ELEMENT_NAME_ENTRY);
		
		posX = POPUP_WIDTH / 2;
		posY += POPUP_HEIGHT / 3;
		int submitWidth = POPUP_WIDTH / 2;
		int submitHeight = POPUP_HEIGHT / 4;
		this.handleTextButton("subm", "move", 15, posX, posY, submitWidth, submitHeight, null, "Submit", CODE_SUBMIT, Color.white, Color.black);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public String[] getSubmitted() {
		while(!ready) {
		};
		return out;
	}

//---  Input Handling   -----------------------------------------------------------------------
	
	private void enableEnd() {
		for(int i = 0; i < out.length; i++) {
			out[i] = this.getStoredText(ELEMENT_NAME_ENTRY + "_" + i);
		}
		ready = true;
	}
	
	@Override
	public void clickAction(int arg0, int arg1, int arg2) {
		if(arg0 == CODE_SUBMIT) {
			enableEnd();
		}
	}

	@Override
	public void clickPressAction(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickReleaseAction(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragAction(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyAction(char arg0) {
		if(arg0 == KEY_ENTER) {
			enableEnd();
		}
	}
	
	@Override
	public void scrollAction(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
