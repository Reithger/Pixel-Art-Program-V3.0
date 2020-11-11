package visual.drawboard;

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
import visual.panel.CanvasPanel;
import visual.panel.ElementPanel;

public class DrawingPage {

	public final static int CODE_HEADER_PRESS = 1;
	public final static int CODE_HEADER_RELEASE = 2;
	public final static int CODE_HEADER_HOLD = 3;
	public final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 12);
	
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
	
	private int lastX;
	
	private int lastY;
	
	private boolean dragging;
	
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
		reference.passOnDraw(x, y, nom);
	}
	
	public void passOnCode(int code, int x, int y, String nom) {
		boolean letgo = true;
		switch(code) {
			case CODE_HEADER_PRESS:
				dragging = true;
				lastX = x;
				lastY = y;
				letgo = false;
				break;
			case CODE_HEADER_HOLD:
				if(dragging) {
					getCorkboard(nom).move(x - lastX, y - lastX);
					lastX = x;
					lastY = y;
				}
				letgo = false;
				break;
			case CODE_HEADER_RELEASE:
				dragging = false;
				letgo = false;
				break;
		}
		if(letgo || code == -1)
			reference.passOnCode(code);
	}
	
	public boolean generateAnimationDisplay(String nom, BufferedImage[] images) {
		DisplayAnimation anim = new DisplayAnimation(nom, images[0].getWidth(), images[0].getHeight(), images, this);
		int[] coords = findOpenSpot(images[0].getWidth(), images[0].getHeight());
		if(coords == null)
			return false;
		anim.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = anim.getPanel();
		displays.put(nom, anim);
		parent.addPanelToWindow(windowName, "animation_" + nom, disp);
		return true;
	}

	public boolean generatePictureDisplay(String nom, Image in) {
		DisplayPicture pic = new DisplayPicture(nom, in.getWidth(null), in.getHeight(null), in);
		int[] coords = findOpenSpot(pic.getWidth(), pic.getHeight());
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0],  y + coords[1]);
		ElementPanel disp = pic.getPanel();
		displays.put(nom, pic);
		parent.addPanelToWindow(windowName, "animation_" + nom, disp);
		return true;
	}
	
	public boolean generatePictureCanvas(String nom, Image in) {
		DrawPicture pic = new DrawPicture(nom, in.getWidth(null), in.getHeight(null), 1, this);
		pic.setImage(in);
		int[] coords = findOpenSpot(in.getWidth(null), in.getHeight(null));
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0], y + coords[1]);
		CanvasPanel canv = pic.getPanel();
		drawings.put(nom, pic);
		parent.addPanelToWindow(windowName, "canvas_" + nom, canv);
		return true;
	}
	
	public boolean generateEmptyPictureCanvas(String nom, int width, int height) {
		DrawPicture pic = new DrawPicture(nom, width, height, 1, this);
		int[] coords = findOpenSpot(width, height);
		if(coords == null)
			return false;
		pic.setLocation(x + coords[0], y + coords[1]);
		CanvasPanel canv = pic.getPanel();
		drawings.put(nom, pic);
		parent.addPanelToWindow(windowName, "canvas_" + nom, canv);
		return true;
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
	
	private int[] findOpenSpot(int width, int height) {
		return new int[] {0, 0}; //TODO: Find an open spot in the screen given other extant displays/canvases to place something
	}
	
}
