package meta;

import java.awt.Color;
import java.awt.Font;

import visual.panel.ElementPanel;

public class HandlePanel extends ElementPanel{

	private final static Font DEFAULT_FONT = new Font("Serif", Font.BOLD,  12);
	
	private boolean frame;
	
	public HandlePanel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void setFrameMode(boolean mode) {
		frame = mode;
	}
	
	public void handleText(String nom, int x, int y, int wid, int hei, String phr) {
		if(!moveElement(nom, x, y)){
			addText(nom, 15, frame, x, y, wid, hei, phr, DEFAULT_FONT, true, true, true);
		}
	}
	
	public void handleImage(String nom, int x, int y, String path, double scale) {
		if(!moveElement(nom, x, y)){
			addImage(nom, 15, frame, x, y, true, path, scale);
		}
	}

	public void handleTextEntry(String nom, int x, int y, int wid, int hei, int cod, String phr) {
		if(!moveElement(nom, x, y)){
			addTextEntry(nom, 15, frame, x, y, wid, hei, cod, phr, DEFAULT_FONT, true, true, true);
		}
	}
	
	public void handleButton(String nom, int x, int y, int wid, int hei, int code) {
		if(!moveElement(nom, x, y)) {
			addButton(nom, 10, frame, x, y, wid, hei, code, true);
		}
	}
	
	public void handleLine(String nom, int x, int y, int x2, int y2, int thck, Color col) {
		if(!moveElement(nom, x, y)) {
			addLine(nom, 20, frame, x, y, x2, y2, thck, col);
		}
	}
	
	public void handleRectangle(String nom, int x, int y, int wid, int hei, Color inside, Color border) {
		if(!moveElement(nom, x, y)) {
			addRectangle(nom, 5, frame, x, y, wid, hei, true, inside, border);
		}
	}
	
	public void handleThickRectangle(String nom, int x, int y, int x2, int y2, Color border, int thick) {
		x += thick / 2;
		y += thick / 2;
		x2 -= thick / 2;
		y2 -= thick / 2;
		if(!moveElement(nom + "_line_1", x, y)) {
			addLine(nom + "_line_1", 5, frame, x, y, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_2", x, y)) {
			addLine(nom + "_line_2", 5, frame, x, y, x, y2, thick, border);
		}
		if(!moveElement(nom + "_line_3", x2, y2)) {
			addLine(nom + "_line_3", 5, frame, x2, y2, x2, y, thick, border);
		}
		if(!moveElement(nom + "_line_4", x2, y2)) {
			addLine(nom + "_line_4", 5, frame, x2, y2, x, y2, thick, border);
		}
	}
	
}
