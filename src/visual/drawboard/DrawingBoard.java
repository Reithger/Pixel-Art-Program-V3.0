package visual.drawboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import visual.View;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;
import visual.settings.PopoutConfirm;

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
		reference.handOffInt(code);
	}
	
	//-- Page Management  -------------------------------------
	
	public void generateSelectBar(int x, int y, int wid, int hei) {
		selectBar = new HandlePanel(x, y, wid, hei) {
			private boolean dragging;
			private int lastX;
			private int lastY;
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				System.out.println(code);
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
						PopoutConfirm pC = new PopoutConfirm(200, 150, "Are you sure?");
						boolean choice = pC.getChoice();
						pC.dispose();
						if(choice) {
							removePage(offCode);
						}
					}
				}
			}
			
			@Override
			public void clickPressBehaviour(int code, int x, int y) {
				dragging = true;
				lastX = x;
				lastY = y;
			}
			
			@Override
			public void dragBehaviour(int code, int x, int y) {
				if(dragging) {
					int difX = x - lastX;
					int difY = y - lastY;
					setOffsetXBounded(getOffsetX() + difX);
					setOffsetYBounded(getOffsetY() + difY);
					lastX = x;
					lastY = y;
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
	
	public void refresh() {
		for(int i : pages.keySet()) {
			parent.hideActiveWindow(pages.get(i).getWindowName());
		}
		parent.showActiveWindow(getCurrentPage().getWindowName());
		drawSelectBar();
	}
	
	public void drawSelectBar() {
		int wid = selectBar.getWidth() / (pages.keySet().size() < SELECT_BAR_MIN_SECTIONS ? SELECT_BAR_MIN_SECTIONS : pages.keySet().size());

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
