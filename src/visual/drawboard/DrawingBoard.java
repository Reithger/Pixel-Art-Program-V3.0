package visual.drawboard;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import visual.View;
import visual.frame.WindowFrame;

public class DrawingBoard {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String BODY_WINDOW_NAME = "body";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private HashMap<Integer, DrawingPage> pages;

	private int active;
	
	private WindowFrame parent;
	
	private View reference;
	
	private int counter;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public DrawingBoard(int inX, int inY, int wid, int hei, WindowFrame par, View ref) {
		x = inX;
		y = inY;
		reference = ref;
		width = wid;
		height = hei;
		pages = new HashMap<Integer, DrawingPage>();
		addNewPage();
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	//-- Input  -----------------------------------------------
	
	public void passOnDraw(int x, int y, String nom) {
		reference.handOffClick(x, y, nom);
	}
	
	public void passOnCode(int code) {
		reference.handOffInt(code);
	}
	
	//-- Page Management  -------------------------------------
	
	public void addNewPage() {
		int next = pages.keySet().size();
		pages.put(next, new DrawingPage(x, y, width, height, formPageName(counter++), parent, this));
		parent.reserveWindow(formPageName(next));
		active = next;
		refresh();
	}
	
	public void removePage(int index) {
		pages.remove(index);
		parent.removeWindow(formPageName(index));
		ArrayList<DrawingPage> order = new ArrayList<DrawingPage>();
		for(DrawingPage p : pages.values()) {
			order.add(p);
		}
		pages = new HashMap<Integer, DrawingPage>();
		for(int i = 0; i < pages.size(); i++) {
			pages.put(i, order.get(i));
		}
		if(active >= index) {
			active--;
		}
		refresh();
	}
	
	public void refresh() {
		for(int i : pages.keySet()) {
			parent.hideActiveWindow(formPageName(i));
		}
		parent.showActiveWindow(formPageName(active));
	}
	
	//-- Generate Things  -------------------------------------
	
	public void generateAnimationDisplay(String nom, BufferedImage[] images) {
		if(!getCurrentPage().generateAnimationDisplay(nom, images)) {
			addNewPage();
			getCurrentPage().generateAnimationDisplay(nom, images);
		}
	}

	public void generatePictureDisplay(String nom, Image in) {
		if(!getCurrentPage().generatePictureDisplay(nom, in)) {
			addNewPage();
			getCurrentPage().generatePictureDisplay(nom, in);
		}
	}
	
	public void generatePictureCanvas(String nom, Image in) {
		if(!getCurrentPage().generatePictureCanvas(nom, in)) {
			addNewPage();
			getCurrentPage().generatePictureCanvas(nom, in);
		}
	}
	
	public void generateEmptyPictureCanvas(String nom, int width, int height) {
		if(!getCurrentPage().generateEmptyPictureCanvas(nom, width, height)) {
			addNewPage();
			getCurrentPage().generateEmptyPictureCanvas(nom, width, height);
		}
	}
	
	//-- Thing Management  ------------------------------------
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setActivePage(int in) {
		active = in;
		refresh();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	private String formPageName(int index) {
		return getWindowName() + "_" + index;
	}
	
	private DrawingPage getCurrentPage() {
		return pages.get(active);
	}
	
	public String getWindowName() {
		return BODY_WINDOW_NAME;
	}
	
}
