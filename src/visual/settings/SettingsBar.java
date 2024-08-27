package visual.settings;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import control.code.CodeReference;
import input.CustomEventReceiver;
import input.manager.actionevent.KeyActionEvent;
import visual.CodeMetaAccess;
import visual.InputHandler;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;
import visual.settings.page.Page;
import visual.settings.page.PageFactory;

public class SettingsBar implements InputHandler, CodeMetaAccess{
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static String MENU_BAR_WINDOW_NAME = "menu bar";
	private final static double RATIO_MENU_SELECTION = 1.0/6;
	private final static Font MENU_FONT = new Font("Serif", Font.BOLD, 12);
	private final static int SELECT_BAR_MIN_SECTIONS = 8;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private WindowFrame parent;
	private HandlePanel menu;
	private InputHandler reference;
	private ArrayList<Page> pages;
	private int activePage;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public SettingsBar(int x, int y, int wid, int hei, WindowFrame par, InputHandler ref) {
		parent = par;
		reference = ref;
		PageFactory.assignCodeReference(this);
		formatPages(x, y + (int)(hei * (RATIO_MENU_SELECTION)), wid, (int)(hei * (1 - RATIO_MENU_SELECTION)));
		menu = generateMenuBar(x, y, wid, (int)(hei * RATIO_MENU_SELECTION));
		drawMenuBar();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void resizeComponent(int newWid, int newHei) {
		menu.resize(newWid, (int)(newHei * RATIO_MENU_SELECTION));
		for(Page p : pages) {
			resizePage(p, p.getPanelXLocation(), (int)(newHei * RATIO_MENU_SELECTION), newWid, (int)(newHei * (1 - RATIO_MENU_SELECTION)));
		}
	}
	
	public void updateTileGridColors(String ref, ArrayList<Color> cols, int[] codes, int active) {
		getActivePage().assignTileGridColors(ref, cols, codes);
		getActivePage().assignTileGridActive(ref, active);
		getActivePage().drawPage();
	}
	
	public void updateTileGridImages(String ref, int[] codes, int active) {
		getActivePage().assignTileGridImages(ref, codes);
		getActivePage().assignTileGridActive(ref, active);
		getActivePage().drawPage();
	}
	
	public void updateTileGridActive(String ref, int active) {
		getActivePage().assignTileGridActive(ref, active);
		getActivePage().drawPage();
	}
	
	public void updateNumericSelector(String ref, int min, int max, int store) {
		getActivePage().assignTileNumericSelectorValues(ref, min, max, store);
		getActivePage().drawPage();
	}
	
	public void refreshActivePage() {
		getActivePage().refresh();
	}
	
	//-- Input  -----------------------------------------------

	public void handleCodeInput(int code, String context) {
		reference.handleCodeInput(code, context);
	}
	
	public void handleDrawInput(int x, int y, int duration, String ref) {
		reference.handleDrawInput(x, y, duration, ref);
	}
	
	public void handleKeyInput(char code, int keyType) {
		reference.handleKeyInput(code, keyType);
	}

	private void formatPages(int x, int y, int wid, int hei) {
		Page.assignReference(this);
		pages = PageFactory.generateStartingPages();
		activePage = 0;
		for(Page p : pages) {
			resizePage(p, x, y, wid, hei);
			parent.addPanelToWindow(p.getName(), p.getName(), p);
		}
		parent.showActiveWindow(pages.get(activePage).getName());
	}
	
	private void resizePage(Page p, int x, int y, int wid, int hei) {
		p.resize(wid, hei);
		p.setLocation(x, y);
		if(menu != null)
			drawMenuBar();
		p.drawPage();
	}
	
	private HandlePanel generateMenuBar(int x, int y, int wid, int hei) {
		HandlePanel p =  new HandlePanel(x, y, wid, hei);
		p.setEventReceiver(new CustomEventReceiver(){
			
			@Override
			public void clickEvent(int code, int x, int y, int clickStart) {
				if(!changePage(code)) {
					reference.handleCodeInput(code, null);
				}
			}
			
			@Override
			public void keyReleaseEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_UP);
				
			}
			
			@Override
			public void keyPressEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY_DOWN);
			}
			
			@Override
			public void keyEvent(char code) {
				reference.handleKeyInput(code, KeyActionEvent.EVENT_KEY);
			}
			
			@Override
			public void mouseWheelEvent(int rotation) {
				
			}
			
		});
		p.getPanel().setBackground(null);
		parent.addPanelToWindow(getMenuBarWindowName(), "menu_bar", p);
		parent.showActiveWindow(getMenuBarWindowName());
		return p;
	}

	private boolean changePage(int code) {
		if(code >= 0 && code < pages.size()) {
			parent.hideActiveWindow(getActivePageName());
			setMenuIndex(code);
			drawMenuBar();
			parent.showActiveWindow(getActivePageName());
			getActivePage().refresh();
			return true;
		}
		return false;
	}
	
	private void drawMenuBar() {
		if(pages.size() == 0) {
			return;
		}
		int distX = menu.getWidth() / (pages.size() < SELECT_BAR_MIN_SECTIONS ? SELECT_BAR_MIN_SECTIONS : pages.size());
		int posX = distX / 2;
		int posY = menu.getHeight() / 2;
		int hei = menu.getHeight();
		menu.removeElementPrefixed("rect_title_");
		for(int i = 0; i < pages.size(); i++) {
			menu.handleText("text_title_" + i, "move", 15, posX, posY, distX, hei, MENU_FONT, pages.get(i).getName());
			menu.handleRectangle("rect_title_" + i, "move", 10, posX, posY, distX, hei, i == activePage ? Color.green : Color.gray, Color.black);
			menu.handleButton("butt_title_" + i, "move", 15, posX, posY, distX, hei, i);
			posX += distX;
		}
	}
	
	public String getTileContents(String ref) {
		return getActivePage().getTileInfo(ref);
	}
	
	//-- Code Meta Access  ------------------------------------
	
	@Override
	public String getCodeImagePath(int code) {
		return CodeReference.getCodeImagePath(code);
	}

	@Override
	public String getCodeLabel(int code) {
		return CodeReference.getCodeLabel(code);
	}

	@Override
	public ArrayList<String> getCodeImagePaths(int[] codes){
		ArrayList<String> out = new ArrayList<String>();
		
		for(int i = 0; i < codes.length; i++) {
			out.add(getCodeImagePath(codes[i]));
		}
		return out;
	}

	@Override
	public ArrayList<String> getCodeLabels(int[] codes){
		ArrayList<String> out = new ArrayList<String>();
		
		for(int i = 0; i < codes.length; i++) {
			out.add(getCodeLabel(codes[i]));
		}
		return out;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void toggleTooltips() {
		getActivePage().toggleTooltips();
	}
	
	public void setMenuIndex(int in) {
		if(in >= 0 && in < pages.size()) {
			activePage = in;
			drawMenuBar();
		}
	}

//---  Getter Methods   -----------------------------------------------------------------------

	private Page getActivePage() {
		return pages.get(activePage);
	}
	
	private String getActivePageName() {
		return pages.get(activePage).getName();
	}
	
	public String[] getPageNames() {
		String[] out = new String[pages.size()];
		for(int i = 0; i < pages.size(); i++) {
			out[i] = pages.get(i).getName();
		}
		return out;
	}
	
	public String getMenuBarWindowName() {
		return MENU_BAR_WINDOW_NAME;
	}

}
