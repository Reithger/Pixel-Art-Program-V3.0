package visual.popouts;

import java.awt.Color;

import visual.composite.popout.PopoutWindow;

public class PopoutConfirm extends PopoutWindow{
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int CODE_YES = 1;
	private final static int CODE_NO = 2;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private volatile boolean choice;
	private volatile boolean ready;

//---  Constructors   -------------------------------------------------------------------------
	
	public PopoutConfirm(int width, int height, String defPhrase) {
		super(width, height);
		drawConfirmation(defPhrase);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void drawConfirmation(String given) {
		int posX = getWidth() / 2;
		int posY = getHeight() / 3;
		int hei = getHeight() / 2;
		int wid = getWidth() * 4 / 5;
		this.handleText("tex", "move", 15, posX, posY, wid, hei, null, given);
		this.handleRectangle("tex_bak", "move", 5, posX, posY, wid, hei, Color.white, Color.black);
		posY += getHeight() * 4 / 9;
		hei = getHeight() / 5;
		this.handleTextButton("yes", "move", 15, posX - getWidth() / 6, posY, getWidth() / 4, hei, null, "Yes", CODE_YES, Color.white, Color.black);
		this.handleTextButton("no", "move", 15, posX + getWidth() / 6, posY, getWidth() / 4, hei, null, "No", CODE_NO, Color.white, Color.black);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public boolean getChoice() {
		while(!ready) {}
		return choice;
	}
	
//---  Input Handling   -----------------------------------------------------------------------
	
	@Override
	public void clickAction(int arg0, int arg1, int arg2) {
		if(arg0 == CODE_YES) {
			choice = true;
			ready = true;
		}
		else if(arg0 == CODE_NO) {
			choice = false;
			ready = true;
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
