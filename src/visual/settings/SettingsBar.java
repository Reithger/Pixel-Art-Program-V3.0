package visual.settings;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import visual.View;
import visual.composite.HandlePanel;
import visual.frame.WindowFrame;
import visual.settings.page.Page;
import visual.settings.page.PageFactory;

public class SettingsBar {
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static String MENU_BAR_WINDOW_NAME = "menu bar";
	private final static double RATIO_MENU_SELECTION = 1.0/6;
	//TODO: Have a custom Page for Animation actions, Image actions, Layer actions that appears contextually
	private final static Font MENU_FONT = new Font("Serif", Font.BOLD, 12);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private WindowFrame parent;
	private HandlePanel menu;
	private View reference;
	private ArrayList<Page> pages;
	private int activePage;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public SettingsBar(int x, int y, int wid, int hei, WindowFrame par, View ref) {
		parent = par;
		reference = ref;
		formatPages(x, y, wid, hei);
		menu = generateMenuBar(x, y, wid, hei);
		par.addPanelToWindow(getMenuBarWindowName(), "menu_bar", menu);
		par.showActiveWindow(getMenuBarWindowName());
		drawMenuBar();
		activePage = 0;
		pages = new ArrayList<Page>();
		PageFactory.assignReference(this);
		pages.add(PageFactory.generateFilePage());
		pages.add(PageFactory.generateDrawingPage());
		pages.add(PageFactory.generateSettingsPage());
		par.showActiveWindow(pages.get(activePage).getName());
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
//-- Input  -----------------------------------------------

	public void passOnCode(int code) {
		reference.handOffInt(code);
	}
	
	private void formatPages(int x, int y, int wid, int hei) {
		for(Page p : pages) {
			p.resize(wid, (int)(hei * (1 - RATIO_MENU_SELECTION)));
			p.setLocation(x, y + (int)(hei * RATIO_MENU_SELECTION));
			parent.addPanelToWindow(p.getName(), p.getName(), p);
			p.drawPage();
		}
	}
	
	private HandlePanel generateMenuBar(int x, int y, int wid, int hei) {
		return new HandlePanel(x, y, wid, (int)(hei * RATIO_MENU_SELECTION)) {
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				if(code >= 0 && code < pages.size()) {
					parent.hideActiveWindow(getActivePageName());
					setMenuIndex(code);
					drawMenuBar();
					parent.showActiveWindow(getActivePageName());
				}
				reference.handOffInt(code);
			}
			
			@Override
			public void keyBehaviour(char code) {
				
			}
			
			@Override
			public void mouseWheelBehaviour(int rotation) {
				
			}
			
		};
	}

	private void drawMenuBar() {
		if(pages.size() == 0) {
			return;
		}
		int distX = menu.getWidth() / pages.size();
		int posX = distX / 2;
		menu.removeElementPrefixed("rect_title_");
		for(int i = 0; i < pages.size(); i++) {
			menu.handleText("text_title_" + i, false, posX, menu.getHeight() / 2, distX, menu.getHeight(), MENU_FONT, pages.get(i).getName());
			menu.handleRectangle("rect_title_" + i, false, 10, posX, menu.getHeight() / 2, distX, menu.getHeight(), i == activePage ? Color.green : Color.gray, Color.black);
			menu.handleButton("butt_title_" + i, false, posX, menu.getHeight() / 2, distX, menu.getHeight(), i);
			posX += distX;
		}
	}
	
	public void passInputCode(int code) {
		reference.handOffInt(code);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setMenuIndex(int in) {
		if(in >= 0 && in < pages.size()) {
			activePage = in;
			drawMenuBar();
		}
	}

//---  Getter Methods   -----------------------------------------------------------------------

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
