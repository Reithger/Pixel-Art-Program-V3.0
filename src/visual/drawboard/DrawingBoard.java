package visual.drawboard;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import visual.View;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;

public class DrawingBoard {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String BODY_WINDOW_NAME = "body";
	private final static int PROPORTION_TOP_SELECT = 10;
	private final static int SELECT_BAR_MIN_SECTIONS = 8;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private HashMap<Integer, DrawingPage> pages;
	
	private HandlePanel selectBar;

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
		selectBar = new HandlePanel(x, y, wid, hei / PROPORTION_TOP_SELECT);
		drawSelectBar();
		par.addPanelToWindow("drawing board", "select bar", selectBar);
		pages = new HashMap<Integer, DrawingPage>();
		parent = par;
		addNewPage();
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
		parent.reserveWindow(formPageName(counter));
		pages.put(next, new DrawingPage(x, y, width, height, formPageName(counter++), parent, this));
		active = next;
		refresh();
	}
	
	public void removePage(int index) {
		parent.removeWindow(pages.get(index).getWindowName());
		pages.remove(index);
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
			parent.hideActiveWindow(pages.get(i).getWindowName());
		}
		parent.showActiveWindow(getCurrentPage().getWindowName());
	}
	
	public void drawSelectBar() {
		int counter = 0;
		int wid = selectBar.getWidth() / (pages.keySet().size() < SELECT_BAR_MIN_SECTIONS ? SELECT_BAR_MIN_SECTIONS : pages.keySet().size());
		for(int i : pages.keySet()) {
			
		}
	}
	
	//-- Generate Things  -------------------------------------
	
	public void generateAnimationDisplay(String nom, Image[] images) {
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
	
	public void generatePictureCanvas(String nom, Color[][] cols) {
		if(!getCurrentPage().generatePictureCanvas(nom, cols)) {
			addNewPage();
			getCurrentPage().generatePictureCanvas(nom, cols);
		}
	}
	
	//-- Thing Management  ------------------------------------
	
	public void updateDisplay(String nom, Image ... images) {
		getCurrentPage().updateDisplay(nom, images);
	}
	
	public void updatePictureCanvas(String nom, int x, int y, Color[][] cols) {
		getCurrentPage().updatePictureCanvas(nom, x, y, cols);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setActivePage(int in) {
		active = in;
		refresh();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getActiveElement() {
		return getCurrentPage().getActiveElement();
	}
	
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
