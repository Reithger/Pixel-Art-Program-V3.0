package visual.drawboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
		if(letgo && code != -1)
			reference.passOnCode(code);
	}
	
	//-- Generate Things  -------------------------------------
	
	public boolean generateAnimationDisplay(String nom, BufferedImage[] images) {
		String useName = getUniqueName("animation_" + nom);
		
		DisplayAnimation anim = new DisplayAnimation(nom, useName, images[0].getWidth(null), images[0].getHeight(null), images, this);
		int[] coords = findOpenSpot(images[0].getWidth(null), images[0].getHeight(null));
		if(coords == null)
			return false;
		anim.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = anim.getPanel();
		displays.put(nom, anim);
		parent.addPanelToWindow(windowName, useName, disp);
		return true;
	}

	public boolean generatePictureDisplay(String nom, BufferedImage in) {
		String useName = getUniqueName("picture_" + nom);
		DisplayPicture pic = new DisplayPicture(nom, useName, in.getWidth(null), in.getHeight(null), in, this);
		int[] coords = findOpenSpot(pic.getWidth(), pic.getHeight());
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = pic.getPanel();
		displays.put(nom, pic);
		parent.addPanelToWindow(windowName, useName, disp);
		return true;
	}
	
	public boolean generatePictureCanvas(String nom, BufferedImage in) {
		String useName = getUniqueName("canvas_" + nom);
		int wid = in.getWidth(null);
		int hei = in.getHeight(null);
		DrawPicture pic = new DrawPicture(nom, useName, in, this);
		int[] coords = findOpenSpot(wid, hei);
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0], y + coords[1]);
		ElementPanel canv = pic.getPanel();
		drawings.put(nom, pic);
		parent.addPanelToWindow(windowName, useName, canv);
		return true;
	}

	//-- Thing Management  ------------------------------------
	
	public void updatePictureCanvas(String nom, int x, int y, Color[][] cols) {
		if(drawings.get(nom) != null)
			drawings.get(nom).updateCanvas(x, y, cols);
	}
	
	public void updateDisplay(String nom, Image[] images, int zoom) {
		if(displays.get(nom) != null)
			displays.get(nom).updateDisplay(images);
	}
	
	public void updateCanvas(String nom, Image[] images, int zoom) {
		if(drawings.get(nom) != null)
			drawings.get(nom).updateCanvasMeta(images, zoom);
	}
	
	public void removeFromDisplay(String nom) {
		parent.removeWindowPanel(windowName, getCorkboard(nom).getPanelName());
		displays.remove(nom);
		drawings.remove(nom);
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
	
	private String getUniqueName(String baseName) {
		String useName = baseName;
		int counter = 1;
		while(displays.get(useName) != null || drawings.get(useName) != null) {
			useName = baseName + "_" + counter++;
		}
		return useName;
	}
	
	public String getWindowName() {
		return windowName;
	}
	
	private int[] findOpenSpot(int width, int height) {
		return new int[] {0, 0}; //TODO: Find an open spot in the screen given other extant displays/canvases to place something
	}
	
}
