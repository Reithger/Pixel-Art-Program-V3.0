package visual.drawboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;

import visual.drawboard.display.DisplayAnimation;
import visual.drawboard.display.DisplayPicture;
import visual.drawboard.draw.DrawPicture;
import visual.drawboard.draw.Drawable;
import visual.drawboard.display.Display;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class DrawingPage {

	public final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
	
	public final static int CODE_HEADER_HOLD = 5;
	public final static int CODE_HEADER_RELEASE = 5;
	public final static int CODE_HEADER_PRESS = 5;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private HashMap<String, Display> displays;
	
	private HashMap<String, Drawable> drawings;
	
	private WindowFrame parent;
	
	private DrawingBoard reference;
	
	private String windowName;
	
	private String active;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawingPage(int inX, int inY, int wid, int hei, String nom, WindowFrame par, DrawingBoard ref) {
		x = inX;
		y = inY;
		windowName = nom;
		reference = ref;
		width = wid;
		height = hei;
		displays = new HashMap<String, Display>();
		drawings = new HashMap<String, Drawable>();
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void passOnDraw(int x, int y, String nom) {
		active = nom;
		reference.passOnDraw(x, y, nom);
	}
	
	public void passOnCode(int code, int x, int y, String nom) {
		active = nom;
		boolean letgo = true;
		if(letgo || code == -1)
			reference.passOnCode(code);
	}
	
	//-- Generate Things  -------------------------------------
	
	public boolean generateAnimationDisplay(String nom, Image[] images) {
		DisplayAnimation anim = new DisplayAnimation(nom, images[0].getWidth(null), images[0].getHeight(null), images, this);
		int[] coords = findOpenSpot(images[0].getWidth(null), images[0].getHeight(null));
		if(coords == null)
			return false;
		anim.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = anim.getPanel();
		displays.put(nom, anim);
		parent.addPanelToWindow(windowName, "animation_" + nom, disp);
		return true;
	}

	public boolean generatePictureDisplay(String nom, Image in) {
		DisplayPicture pic = new DisplayPicture(nom, in.getWidth(null), in.getHeight(null), in, this);
		int[] coords = findOpenSpot(pic.getWidth(), pic.getHeight());
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = pic.getPanel();
		displays.put(nom, pic);
		parent.addPanelToWindow(windowName, "animation_" + nom, disp);
		return true;
	}
	
	public boolean generatePictureCanvas(String nom, Color[][] cols) {
		int wid = cols.length;
		int hei = cols[0].length;
		DrawPicture pic = new DrawPicture(nom, wid, hei, this);
		pic.updateCanvas(0, 0, cols);
		int[] coords = findOpenSpot(wid, hei);
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0], y + coords[1]);
		ElementPanel canv = pic.getPanel();
		drawings.put(nom, pic);
		parent.addPanelToWindow(windowName, "canvas_" + nom, canv);
		return true;
	}

	//-- Thing Management  ------------------------------------
	
	public void updatePictureCanvas(String nom, int x, int y, Color[][] cols) {
		if(drawings.get(nom) != null)
			drawings.get(nom).updateCanvas(x, y, cols);
		else {
			generatePictureCanvas(nom, cols);
		}
	}
	
	public void updateDisplay(String nom, Image ... images) {
		if(displays.get(nom) != null)
			displays.get(nom).updateDisplay(images);
		else {
			if(images.length > 1) {
				generateAnimationDisplay(nom, images);
			}
			else {
				generatePictureDisplay(nom, images[0]);
			}
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getActiveElement() {
		return active;
	}
	
	private Corkboard getCorkboard(String nom) {
		if(displays.get(nom) != null){
			return displays.get(nom);
		}
		if(drawings.get(nom) != null){
			return drawings.get(nom);
		}
		return null;
	}
	
	public String getWindowName() {
		return windowName;
	}
	
	private int[] findOpenSpot(int width, int height) {
		return new int[] {0, 0}; //TODO: Find an open spot in the screen given other extant displays/canvases to place something
	}
	
}
