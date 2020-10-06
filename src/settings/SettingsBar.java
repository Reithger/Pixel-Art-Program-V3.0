package settings;

import java.awt.Color;

import meta.HandlePanel;
import settings.pages.DrawingPage;
import settings.pages.FilePage;
import settings.pages.Page;
import visual.frame.WindowFrame;

public class SettingsBar {
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static String MENU_BAR_WINDOW_NAME = "menu bar";
	private final static double RATIO_MENU_SELECTION = 1.0/6;
	private final static Page[] PAGES = new Page[] {new FilePage(), new DrawingPage()}; //, new SettingsPage()};
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private WindowFrame reference;
	private HandlePanel menu;
	private int activePage;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public SettingsBar(int x, int y, int wid, int hei, WindowFrame ref) {
		reference = ref;
		formatPages(x, y, wid, hei);
		menu = generateMenuBar(x, y, wid, hei);
		ref.reservePanel(getMenuBarWindowName(), "menu_bar", menu);
		ref.showActiveWindow(getMenuBarWindowName());
		drawMenuBar();
		activePage = 0;
		ref.showActiveWindow(PAGES[activePage].getName());
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void formatPages(int x, int y, int wid, int hei) {
		for(Page p : PAGES) {
			p.resize(wid, (int)(hei * (1 - RATIO_MENU_SELECTION)));
			p.setLocation(x, y + (int)(hei * RATIO_MENU_SELECTION));
			reference.reservePanel(p.getName(), p.getName(), p);
			p.drawPage();
		}
	}
	
	private HandlePanel generateMenuBar(int x, int y, int wid, int hei) {
		return new HandlePanel(x, y, wid, (int)(hei * RATIO_MENU_SELECTION)) {
			
			@Override
			public void clickBehaviour(int code, int x, int y) {
				System.out.println(code);
				if(code >= 0 && code < PAGES.length) {
					reference.hideActiveWindow(PAGES[activePage].getName());
					setMenuIndex(code);
					drawMenuBar();
					reference.showActiveWindow(PAGES[activePage].getName());
				}
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
		if(PAGES.length == 0) {
			return;
		}
		int distX = menu.getWidth() / PAGES.length;
		int posX = distX / 2;
		menu.removeElementPrefixed("rect_title_");
		for(int i = 0; i < PAGES.length; i++) {
			menu.handleText("text_title_" + i, posX, menu.getHeight() / 2, distX, menu.getHeight(), PAGES[i].getName());
			menu.handleRectangle("rect_title_" + i, posX, menu.getHeight() / 2, distX, menu.getHeight(), i == activePage ? Color.green : Color.gray, Color.black);
			menu.handleButton("butt_title_" + i, posX, menu.getHeight() / 2, distX, menu.getHeight(), i);
			posX += distX;
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setMenuIndex(int in) {
		if(in >= 0 && in < PAGES.length) {
			activePage = in;
			drawMenuBar();
		}
	}

//---  Getter Methods   -----------------------------------------------------------------------

	public String[] getPageNames() {
		String[] out = new String[PAGES.length];
		for(int i = 0; i < PAGES.length; i++) {
			out[i] = PAGES[i].getName();
		}
		return out;
	}
	
	public String getMenuBarWindowName() {
		return MENU_BAR_WINDOW_NAME;
	}

}
