package drawsurface;

import java.util.ArrayList;

import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class DrawingBoard {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String BODY_WINDOW_NAME = "body";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private ArrayList<Display> displays;
	
	private WindowFrame parent;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawingBoard(int inX, int inY, int wid, int hei, WindowFrame ref) {
		x = inX;
		y = inY;
		width = wid;
		height = hei;
		displays = new ArrayList<Display>();
		parent = ref;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void generateAnimationDisplay(int xP, int yP, String filePath) {
		Animation anim = new Animation();
		anim.importAnimation(filePath);
		ElementPanel disp = anim.generateDisplay(xP,  yP);
		parent.reservePanel("default", "animation_" + filePath, disp);
	}
	
	public void generatePictureDisplay(int xP, int yP, String filePath) {
		
	}
	
	public void generateLayerDisplay(int xP, int yP, String filePath) {
		
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getWindowName() {
		return BODY_WINDOW_NAME;
	}
	
}
