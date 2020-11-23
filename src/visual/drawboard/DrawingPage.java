package visual.drawboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import visual.drawboard.corkboard.Corkboard;
import visual.drawboard.corkboard.DisplayAnimation;
import visual.drawboard.corkboard.DisplayPicture;
import visual.drawboard.corkboard.DrawPicture;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class DrawingPage {

	public final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private HashMap<String, Corkboard> displays;
	
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
		displays = new HashMap<String, Corkboard>();
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
		displays.put(nom, pic);
		parent.addPanelToWindow(windowName, pic.getPanelName(), canv);
		return true;
	}

	//-- Thing Management  ------------------------------------
	
	public void rename(String old, String newName) {
		Corkboard c = getCorkboard(old);
		displays.remove(c.getName());
		c.setName(newName);
		displays.put(c.getName(), c);
		parent.removeWindowPanel(windowName, c.getPanelName());
		c.setPanelName(getUniqueName(c.getName()));
		parent.addPanelToWindow(windowName, c.getPanelName(), c.getPanel());
		c.updatePanel();
		if(active.equals(old)) {
			active = newName;
		}
	}
	
	public void resetLocation(String name) {
		if(getCorkboard(name) != null)
			getCorkboard(name).setLocation(x, y);
	}
	
	public void duplicate(String old, String nom) {
		Corkboard d = displays.get(old);
		Corkboard n = d.duplicate(nom, getUniqueName(nom));
		ElementPanel disp = n.getPanel();
		disp.setLocation(x,  y);
		displays.put(nom, n);
		parent.addPanelToWindow(windowName, n.getPanelName(), disp);
	}

	public void updateDisplay(String nom, BufferedImage[] images, int zoom) {
		if(displays.get(nom) != null)
			displays.get(nom).updateImages(images, zoom);
	}
	
	public void updatePictureCanvas(String nom, int x, int y, Color[][] cols) {
		if(displays.get(nom) != null)	//TODO: Figure out how to do this without a cast
			((DrawPicture)(displays.get(nom))).updateCanvas(x, y, cols);
	}

	public void removeFromDisplay(String nom) {
		parent.removeWindowPanel(windowName, getCorkboard(nom).getPanelName());
		displays.remove(nom);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getOriginX() {
		return x;
	}
	
	public int getOriginY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getActiveElement() {
		return active;
	}
	
	private Corkboard getCorkboard(String nom) {
		return displays.get(nom);
	}
	
	private String getUniqueName(String baseName) {
		String useName = baseName;
		int counter = 1;
		while(displays.get(useName) != null) {
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
