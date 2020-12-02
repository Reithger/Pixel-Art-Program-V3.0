package visual.drawboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import misc.Canvas;
import visual.PopoutConfirm;
import visual.View;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;

public class DrawingBoard {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String BODY_WINDOW_NAME = "body";
	private final static int PROPORTION_TOP_SELECT = 25;
	private final static int SELECT_BAR_MIN_SECTIONS = 12;
	private final static Font MENU_FONT = new Font("Serif", Font.BOLD, 12);
	private final static int CODE_NEW_PAGE = 500;
	
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
		pages = new HashMap<Integer, DrawingPage>();
		parent = par;
		generateSelectBar(inX, inY, wid, hei / PROPORTION_TOP_SELECT);
		drawSelectBar();
		addNewPage();
		par.addPanelToWindow("drawing board", "select bar", selectBar);
		par.showActiveWindow("drawing board");
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	//-- Input  -----------------------------------------------
	
	public void passOnDraw(int x, int y, String nom) {
		reference.handOffClick(x, y, nom);
	}
	
	public void passOnCode(int code) {
		reference.handOffInt(code, getActiveElement());
	}
	
	//-- Page Management  -------------------------------------
	
	public void generateSelectBar(int x, int y, int wid, int hei) {
		selectBar = new HandlePanel(x, y, wid, hei) {
			private boolean dragging;
			private int lastX;
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				if(code == CODE_NEW_PAGE) {
					addNewPage();
				}
				else if(code >= 0 && code < pages.keySet().size()) {
					active = code;
					refresh();
				}
				else {
					int offCode = code - pages.keySet().size();
					if(offCode >= 0 && offCode < pages.keySet().size()) {
						confirmPageDeletion(offCode);
					}
				}
			}
			
			@Override
			public void clickPressBehaviour(int code, int x, int y) {
				dragging = true;
				lastX = x;
			}
			
			@Override
			public void dragBehaviour(int code, int x, int y) {
				if(dragging) {
					int difX = x - lastX;
					setOffsetXBounded(getOffsetX() + difX);
					lastX = x;
				}
			}
			
			@Override
			public void clickReleaseBehaviour(int code, int x, int y) {
				dragging = false;
			}
		};
		selectBar.setPriority(5);
		selectBar.setScrollBarVertical(false);
		selectBar.setScrollBarHorizontal(false);
	}

	public void addNewPage() {
		int next = pages.keySet().size();
		parent.reserveWindow(formPageName(counter));
		pages.put(next, new DrawingPage(x, y + height / PROPORTION_TOP_SELECT, width, height - height / PROPORTION_TOP_SELECT, formPageName(counter++), parent, this));
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
		for(int i = 0; i < order.size(); i++) {
			pages.put(i, order.get(i));
		}
		if(active >= index) {
			active--;
		}
		refresh();
	}
	
	private void confirmPageDeletion(int offCode) {
		PopoutConfirm pC = new PopoutConfirm(200, 150, "Are you sure?");
		boolean choice = pC.getChoice();
		pC.dispose();
		if(choice) {
			removePage(offCode);
		}
	}
	
	public void refresh() {
		for(int i : pages.keySet()) {
			parent.hideActiveWindow(pages.get(i).getWindowName());
		}
		parent.showActiveWindow(getCurrentPage().getWindowName());
		drawSelectBar();
	}
	
	public void drawSelectBar() {
		int wid = selectBar.getWidth() / SELECT_BAR_MIN_SECTIONS;

		int posX = wid / 2;
		int hei = selectBar.getHeight() * 14/15;
		int butSize = hei / 2;
		selectBar.removeElementPrefixed("page_");
		for(int i = 0; i < pages.keySet().size(); i++) {
			selectBar.handleTextButton("page_" + i, false, posX, hei / 2, wid, hei, MENU_FONT, "Page " + (i + 1), i, i == active ? Color.green : Color.gray, Color.black);
			selectBar.addImage("page_close_" + i, 20, false, posX + wid / 2 - butSize / 2, butSize / 2, butSize, 2 * hei / 3, true, "/assets/placeholder.png", true);
			selectBar.addButton("page_close_butt_" + i, 20, false, posX + wid / 2 - butSize / 2, butSize/2, butSize, 2 * hei / 3, null, i + pages.keySet().size(), true);
			posX += wid;
		}
		selectBar.handleTextButton("add_page", false, posX, hei / 2, wid, hei, MENU_FONT, "+New Page", CODE_NEW_PAGE, Color.gray, Color.black);
	}
	
	//-- Generate Things  -------------------------------------
	
	public void generateAnimationDisplay(String nom, Canvas[] images) {
		if(!getCurrentPage().generateAnimationDisplay(nom, images)) {
			addNewPage();
			getCurrentPage().generateAnimationDisplay(nom, images);
		}
	}

	public void generatePictureDisplay(String nom, Canvas in) {
		if(!getCurrentPage().generatePictureDisplay(nom, in)) {
			addNewPage();
			getCurrentPage().generatePictureDisplay(nom, in);
		}
	}
	
	//-- Thing Management  ------------------------------------
	
	public void rename(HashMap<String, String> mappings) {
		getCurrentPage().rename(mappings);
	}
	
	public void duplicateThing(String old, String nom) {
		getCurrentPage().duplicate(old, nom);
	}
	
	public void updateDisplay(String nom, Canvas[] images, int zoom) {
		getCurrentPage().updateDisplay(nom, images, zoom);
	}
	
	public void addAnimation(String nom, Canvas[] imgs) {
		//TODO
	}
	
	public void removeFromDisplay(String nom) {
		getCurrentPage().removeFromDisplay(nom);
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
