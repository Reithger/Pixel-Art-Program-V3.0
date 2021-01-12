package visual.drawboard;

import java.awt.Font;
import java.util.HashMap;

import control.CodeReference;
import control.InputHandler;
import misc.Canvas;
import visual.drawboard.corkboard.Corkboard;
import visual.drawboard.corkboard.CorkboardGenerator;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class DrawingPage implements InputHandler{

//---  Constants   ----------------------------------------------------------------------------
	
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
	
	public void resizeComponent(int newWid, int newHei) {
		width = newWid;
		height = newHei;
	}
	
	public void reposition(int newX, int newY) {
		x = newX;
		y = newY;
		for(Corkboard c : displays.values()) {
			handleCodeInput(Corkboard.CODE_CHECK_POSITION, c.getName());
		}
	}
	
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
			case CodeReference.CODE_MAXIMIZE_CANVAS:
				Corkboard max = getCorkboard(nom);
				max.setLocation(x, y);
				max.resizePanel(width, height);
				break;
			default:
				reference.handleCodeInput(code, nom);
				break;
		}
	}
	
	public void handleKeyInput(char code, int keyType) {
		reference.handleKeyInput(code, keyType);
	}

	//-- Generate Things  -------------------------------------
	
	public void generateAnimationDisplay(String nom, Canvas[] images) {
		Corkboard anim = CorkboardGenerator.generateDisplayAnimation(nom, getUniqueName("animation_" + nom), images);
		setupCorkboard(anim);
	}

	public void generatePictureDisplay(String nom, Canvas in) {
		Corkboard pic = CorkboardGenerator.generateDisplayPicture(nom, getUniqueName("picture_" + nom), in);
		setupCorkboard(pic);
	}
	
	private void setupCorkboard(Corkboard newCork) {
		newCork.setReference(this);
		newCork.setLocation(x + 2,  y + 1);
		ElementPanel disp = newCork.getPanel();
		displays.put(newCork.getName(), newCork);
		parent.addPanelToWindow(windowName, newCork.getPanelName(), disp);
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
	
	public void setContentLock(String nom, boolean set) {
		getCorkboard(nom).setContentLocked(set);
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
	
}
