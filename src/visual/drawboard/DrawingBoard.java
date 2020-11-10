package visual.drawboard;

import java.util.ArrayList;

import visual.drawboard.display.DisplayAnimation;
import visual.drawboard.display.Display;
import visual.drawboard.display.Layer;
import visual.frame.WindowFrame;
import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;

public class DrawingBoard {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String BODY_WINDOW_NAME = "body";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private int count;
	
	private ArrayList<Display> displays;
	//TODO: Allow multiple pages of edited Displays; have a paging system to use different windows to load in/out
	private Display selected;
	
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
		DisplayAnimation anim = new DisplayAnimation();
		anim.importAnimation(filePath);
		ElementPanel disp = anim.generateDisplay(x + xP,  y + yP);
		parent.addPanelToWindow(BODY_WINDOW_NAME, "animation_" + filePath, disp);
	}
	
	public void generatePictureDisplay(int xP, int yP, String filePath) {
		
	}
	
	public void generateLayerDisplay(int xP, int yP, String filePath) {
		
	}
	
	public void generatePictureCanvas(int xP, int yP, String filePath) {
		
	}
	
	public void generateLayerCanvas(int xP, int yP, String filePath) {
		
	}
	
	public void generateEmptyPictureCanvas(int xP, int yP) {
		
	}
	
	public void generateEmptyLayerCanvas(int xP, int yP) {
		Layer l = new Layer(300, 300, 0);
		CanvasPanel use = l.generateCanvas(xP, yP);
		parent.addPanelToWindow(BODY_WINDOW_NAME, "canvas_" + count++, use);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getWindowName() {
		return BODY_WINDOW_NAME;
	}
	
}
