package visual.popouts;

import java.awt.Color;

import visual.composite.popout.PopoutWindow;

public class PopoutColorDesigner extends PopoutWindow{

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String[] SUBMIT_NAMES = new String[] {"Red", "Green", "Blue", "Alpha"};
	private final static int CODE_COLOR_MAKE = 55;
	private final static int CODE_COLOR_SUBMIT = 56;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Color currColor;
	
	private volatile boolean ready;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public PopoutColorDesigner(int wid, int hei, Color defCol) {
		super(wid, hei);
		if(defCol == null) {
			defCol = Color.white;
		}
		currColor = defCol;
		ready = false;
		drawPage();
	}

//---  Operations   ---------------------------------------------------------------------------
	
	private void drawPage() {
		int rows = SUBMIT_NAMES.length + 1;
		int posX = getWidth() / 2;
		int posY = getHeight() / (rows + 1);
		
		handleRectangle("col", false, 1, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), currColor, currColor);
		
		int[] vals = getColorValues();
		int hei = getHeight() / (rows + 2);
		
		for(int i = 0; i < SUBMIT_NAMES.length; i++) {
			int wid = getWidth() / 4;
			handleText(SUBMIT_NAMES[i] + "_tex", false, posX - getWidth() / 6, posY, wid, hei, null, SUBMIT_NAMES[i]);
			handleRectangle(SUBMIT_NAMES[i] + "_rect", false, 5, posX + getWidth() / 6, posY, wid, hei, Color.white, Color.black);
			handleTextEntry(SUBMIT_NAMES[i], false, posX + getWidth() / 6, posY, wid, hei, i, null, ""+vals[i]);
			posY += getHeight() / (rows + 1);
		}
		posX = getWidth() / 2;
		int size = getWidth() / 3;
		this.handleTextButton("txButCol", false, posX - getWidth() / 4, posY, size, hei, null, "Generate", CODE_COLOR_MAKE, Color.white, Color.black);
		this.handleTextButton("txButSub", false, posX + getWidth() / 4, posY, size, hei, null, "Submit", CODE_COLOR_SUBMIT, Color.white, Color.black);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public Color getChoice() {
		while(!ready) {}
		return currColor;
	}

	private int[] getColorValues() {
		return new int[] {currColor.getRed(), currColor.getGreen(), currColor.getBlue(), currColor.getAlpha()};
	}
	
//---  Input Handling   -----------------------------------------------------------------------
	
	@Override
	public void clickAction(int arg0, int arg1, int arg2) {
		switch(arg0) {
			case CODE_COLOR_MAKE:
				try {
					int r = Integer.parseInt(this.getStoredText(SUBMIT_NAMES[0]));
					int g = Integer.parseInt(this.getStoredText(SUBMIT_NAMES[1]));
					int b = Integer.parseInt(this.getStoredText(SUBMIT_NAMES[2]));
					int a = Integer.parseInt(this.getStoredText(SUBMIT_NAMES[3]));
					currColor = new Color(r % 256, g % 256, b % 256, a % 256);
					this.removeElementPrefixed("col");
					drawPage();
				}
				catch(Exception e) {
					int vals[] = getColorValues();
					for(int i = 0; i < vals.length; i++) {
						setStoredText(SUBMIT_NAMES[i], ""+vals[i]);
					}
				}
				break;
			case CODE_COLOR_SUBMIT:
				ready = true;
				break;
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
