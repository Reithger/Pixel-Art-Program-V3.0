package visual;

import java.awt.Color;

import visual.composite.popout.PopoutWindow;

public class PopoutInputRequest extends PopoutWindow{

	private final static int POPUP_WIDTH = 300;
	private final static int POPUP_HEIGHT = 300;
	private final static int CODE_SUBMIT = 5;
	
	public PopoutInputRequest(String text) {
		super(POPUP_WIDTH, POPUP_HEIGHT);
		this.handleText("tex", false, POPUP_WIDTH / 2, POPUP_HEIGHT / 3, POPUP_WIDTH * 3 / 4, POPUP_HEIGHT / 2, null, text);
		this.handleTextButton("subm", false, POPUP_WIDTH / 2, POPUP_HEIGHT * 2 / 3, POPUP_WIDTH / 3, POPUP_HEIGHT / 3, null, "Submit", CODE_SUBMIT, Color.white, Color.black);
	}

	@Override
	public void clickAction(int arg0, int arg1, int arg2) {
		if(arg0 == CODE_SUBMIT) {
			//TODO Asynchronous problems, need to fix SVI
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollAction(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
