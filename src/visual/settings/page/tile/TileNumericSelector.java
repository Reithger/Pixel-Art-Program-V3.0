package visual.settings.page.tile;

import java.awt.Color;
import java.util.ArrayList;

import visual.composite.HandlePanel;

public class TileNumericSelector extends Tile {

//---  Constants   ----------------------------------------------------------------------------
	
	private static int ENTRY_CODE = -1000;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private String label;
	
	private int minVal;
	private int maxVal;
	private int storedValue;
	
	private int decCode;
	private int incCode;
	private int setCode;
	private int entryCode;
	private int sliderCode;
	
	private int lineStart;
	
	private String referenceEntry;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public TileNumericSelector(String inLabel, int min, int max, int decrem, int increm, int set) {
		label = inLabel;
		minVal = min;
		maxVal = max;
		decCode = decrem;
		incCode = increm;
		setCode = set;
		entryCode = ENTRY_CODE--;
		sliderCode = ENTRY_CODE--;
		storedValue = minVal;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public boolean dragTileProcess(int code, int x, int y) {
		int len = getHeight() * 2 / 3;

		int dX = x - lineStart;
		double prop = (dX / (double)len);
		
		setStoredValue(minVal + (int)(prop * (maxVal - minVal)));
		return true;
	}
	
	@Override
	public void drawTile(int x, int y, HandlePanel p) {
		logPosition(x, y);
		p.setElementStoredText(referenceEntry, ""+storedValue);
		int posX = x;
		int posY = y - (getHeight() / 3) / 2;
		int size = getHeight() / 3;
		int iconSize = size / 2;
		drawLabel(label, x, y, p);
		p.handleImageButton("gr_" + label + "_dec", "move", 15, posX, posY, iconSize, iconSize, "./assets/placeholder.png", decCode);
		posX += size;
		p.handleRectangle("gr_" + label + "_rect", "move", 5, posX, posY, size, size, Color.white, Color.black);
		referenceEntry = "gr_" + label + "_txent";
		p.handleTextEntry(referenceEntry, "move", 15, posX, posY, size, size, entryCode, null, ""+storedValue);
		posX += size;
		p.handleImageButton("gr_" + label + "inc", "move", 15, posX, posY, iconSize, iconSize, "./assets/placeholder.png", incCode);
		
		posX = x;
		posY += size;
		
		lineStart = posX;
		p.handleLine("gr_" + label + "_line", "move", 5, posX, posY, posX + 2 * size, posY, 2, Color.black);
		p.handleButton("gr_" + label + "_slider_detect", "move", 15, posX + size, posY - 2, 2 * size, 4, sliderCode);
		double prop = (double)(storedValue - minVal) / (double)(maxVal - minVal);

		posX += (int)(prop * 2 * size);
		p.handleImage("gr_" + label + "_slider", "move", 15, posX, posY, iconSize, size, true, "./assets/placeholder.png");
	}
	
//---  Getter Methods   -----------------------------------------------------------------------

	@Override
	public ArrayList<Integer> getAssociatedCodes() {
		ArrayList<Integer> out = new ArrayList<Integer>();
		out.add(decCode);
		out.add(incCode);
		out.add(setCode);
		out.add(sliderCode);
		out.add(entryCode);
		return out;
	}

	@Override
	public String getInfo() {
		return ""+storedValue;
	}

	@Override
	public int getTileWidth() {
		return getHeight() * 4 / 6;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setValues(int min, int max, int display) {
		minVal = min;
		maxVal = max;
		setStoredValue(display);
	}
	
	private void setStoredValue(int in) {
		in = in < minVal ? minVal : in;
		in = in > maxVal ? maxVal : in;
		storedValue = in;
	}
	
}
