package technical;

import java.awt.Color;
import java.awt.Font;

import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class Options{
	
	private final static double VERT_RATIO_ADMIN = 1.0 / 5;
	
	private final static double ADMIN_TAB_HORIZ_RATIO = 1.0 / 8;
	
	private final static String[] ADMIN_CATEGORIES = new String[] {"File", "Edit", "Pen"};

	private final static String[][] OPTION_CONTENTS = new String[][] {
		{},
		{},
		{}
	};
	
	private HandlePanel admin;
	
	private HandlePanel options;
	
	private int activeAdmin;
	
	public Options(int x, int y, int wid, int hei, WindowFrame ref) {
		activeAdmin = 0;
		admin = new HandlePanel(x, y, wid, (int)(hei * VERT_RATIO_ADMIN)) {
			@Override
			public void clickBehaviour(int code, int x, int y) {
				if(code >= 0 && code < ADMIN_CATEGORIES.length) {
					activeAdmin = code;
					admin.removeElementPrefixed("admin_tab_");
				}
				drawAdminPanel();
			}
		};
		admin.setScrollBarHorizontal(false);
		admin.setScrollBarVertical(false);
		ref.reservePanel("default", "admin", admin);
		drawAdminPanel();
		options = new HandlePanel(x, y + (int)(hei * VERT_RATIO_ADMIN), wid, (int)(hei * (1 - VERT_RATIO_ADMIN))) {
			@Override
			public void clickBehaviour(int code, int x, int y) {
				
			}
		};
		options.setScrollBarVertical(false);
		ref.reservePanel("default", "options", options);
		drawOptionsPanel();
	}
	
	private void drawOptionsPanel() {
		int wid = options.getWidth();
		int hei = options.getHeight();
		options.handleLine("lin", 0, 0, wid, hei, 4, Color.black);
	}
	
	private void drawAdminPanel() {
		int wid = admin.getWidth();
		int hei = admin.getHeight();
		int tabWid = (int)(admin.getWidth() * ADMIN_TAB_HORIZ_RATIO);
		for(int i = 0; i < ADMIN_CATEGORIES.length; i++) {
			System.out.println(i);
			drawAdminTab(tabWid / 2 + i * tabWid, i, ADMIN_CATEGORIES[i], i == activeAdmin);
		}
		admin.handleLine("lin", 0, hei, wid, hei, 4, Color.black);
	}
	
	private void drawAdminTab(int x, int code, String tex, boolean active) {
		admin.handleText("admin_tab_text_" + tex, x, admin.getHeight() / 2, (int)(admin.getWidth() * ADMIN_TAB_HORIZ_RATIO), admin.getHeight(), new Font("Serif", Font.BOLD, 16), tex);
		admin.handleButton("admin_tab_butt_" + tex, x, admin.getHeight() / 2, (int)(admin.getWidth() * ADMIN_TAB_HORIZ_RATIO), admin.getHeight(), code);
		admin.handleRectangle("admin_tab_rect_" + tex, x, admin.getHeight() / 2, (int)(admin.getWidth() * ADMIN_TAB_HORIZ_RATIO), admin.getHeight(), active ? Color.green : Color.gray, Color.black);
	}
	
	public ElementPanel getOptionsPanel() {
		return options;
	}
	
	public class HandlePanel extends ElementPanel {

		private final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 16);
		
		public HandlePanel(int x, int y, int width, int height) {
			super(x, y, width, height);
		}
		
		public void handleText(String nom, int x, int y, int wid, int hei, Font inF, String phr) {
			if(!moveElement(nom, x, y)){
				addText(nom, 15, false, x, y, wid, hei, phr, inF, true, true, true);
			}
		}
		
		public void handleImage(String nom, int x, int y, String path, double scale) {
			if(!moveElement(nom, x, y)){
				addImage(nom, 15, false,  x, y, true, path, scale);
			}
		}

		public void handleTextEntry(String nom, int x, int y, int wid, int hei, int cod, String phr) {
			if(!moveElement(nom, x, y)){
				addTextEntry(nom, 15, false, x, y, wid, hei, cod, phr, DEFAULT_FONT, true, true, true);
			}
		}
		
		public void handleButton(String nom, int x, int y, int wid, int hei, int code) {
			if(!moveElement(nom, x, y)) {
				addButton(nom, 10, false, x, y, wid, hei, code, true);
			}
		}
		
		public void handleLine(String nom, int x, int y, int x2, int y2, int thck, Color col) {
			if(!moveElement(nom, x, y)) {
				addLine(nom, 20, false, x, y, x2, y2, thck, col);
			}
		}
		
		public void handleRectangle(String nom, int x, int y, int wid, int hei, Color inside, Color border) {
			if(!moveElement(nom, x, y)) {
				addRectangle(nom, 5, false, x, y, wid, hei, true, inside, border);
			}
		}
		
	}
	
}
