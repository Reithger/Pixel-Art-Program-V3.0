package visual.drawboard;

import java.awt.Font;
import java.util.HashMap;

import control.InputHandler;
import misc.Canvas;
import visual.drawboard.corkboard.Corkboard;
import visual.drawboard.corkboard.DisplayAnimation;
import visual.drawboard.corkboard.DisplayPicture;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class DrawingPage implements InputHandler{

	public final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private HashMap<String, Corkboard> displays;
	
	private WindowFrame parent;
	
	private InputHandler reference;
	
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
	
	public void handleDrawInput(int x, int y, int duration, String nom) {
		active = nom;
		reference.handleDrawInput(x, y, duration, nom);
	}
	
	public void handleCodeInput(int code, String nom) {
		active = nom;
		switch(code) {
			case Corkboard.CODE_CHECK_POSITION:
				Corkboard c = getCorkboard(nom);
				int cX = c.getPositionX();
				int cY = c.getPositionY();
				cX = cX < x ? x : cX;
				cY = cY < y ? y : cY;
				cX = cX + c.getSidebarWidth() > x + width ? x + width - c.getSidebarWidth() : cX;
				cY = cY + c.getHeaderHeight() > y + height ? y + height - c.getHeaderHeight() : cY;
				c.setLocation(cX, cY);
				break;
			default:
				reference.handleCodeInput(code, nom);
				break;
		}
	}
	
	public void handleKeyInput(char code) {
		reference.handleKeyInput(code);
	}

	//-- Generate Things  -------------------------------------
	
	public boolean generateAnimationDisplay(String nom, Canvas[] images) {
		String useName = getUniqueName("animation_" + nom);
		
		DisplayAnimation anim = new DisplayAnimation(nom, useName, images, this);
		int[] coords = findOpenSpot(anim.getContentWidth(), anim.getContentHeight());
		if(coords == null)
			return false;
		anim.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = anim.getPanel();
		displays.put(nom, anim);
		parent.addPanelToWindow(windowName, useName, disp);
		return true;
	}

	public boolean generatePictureDisplay(String nom, Canvas in) {
		String useName = getUniqueName("picture_" + nom);
		DisplayPicture pic = new DisplayPicture(nom, useName, in, this);
		int[] coords = findOpenSpot(pic.getContentWidth(), pic.getContentHeight());
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = pic.getPanel();
		displays.put(nom, pic);
		parent.addPanelToWindow(windowName, useName, disp);
		return true;
	}

	//-- Thing Management  ------------------------------------
	
	public void rename(HashMap<String, String> mappings) {
		for(String s : mappings.keySet()) {
			rename(s, mappings.get(s));
		}
	}
	
	private void rename(String old, String newName) {
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

	public void updateDisplay(String nom, Canvas[] images, int zoom) {
		Corkboard c = getCorkboard(nom);
		if(c != null) {
			c.setZoom(zoom);
			c.updateImages(images);
		}
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
