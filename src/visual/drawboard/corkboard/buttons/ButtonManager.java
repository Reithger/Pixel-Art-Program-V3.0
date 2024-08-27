package visual.drawboard.corkboard.buttons;

import java.util.ArrayList;
import java.util.HashSet;

public class ButtonManager {

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<CorkboardButton> currButtons;
	
	private boolean display;

//---  Constructors   -------------------------------------------------------------------------
	
	public ButtonManager() {
		currButtons = new ArrayList<CorkboardButton>();
		display = true;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addButton(int code, String path, String label) {
		currButtons.add(new CorkboardButton(code, path, label));
	}
	
	public int removeButton(int code) {
		for(int i = 0; i < currButtons.size(); i++) {
			if(currButtons.get(i).getCode() == code) {
				currButtons.remove(i);
				return i;
			}
		}
		return -1;
	}
	
	public void moveButton(int positOne, int positDestination) {
		//TODO: Get the whole thing figured out
	}
	
	public void toggleDisplay() {
		display = !display;
	}
	
	public void clear() {
		currButtons.clear();
	}
	
	public boolean contains(int in) {
		return getCodes().contains(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * For some reason this needs to return a non-zero value
	 * when nothing should be drawn.
	 * 
	 * If it just returns 0, the visuals aren't removed. -1 or 1,
	 * they are. It's weird.
	 * 
	 * So, this function returns the number of buttons currently
	 * assigned to this ButtonManager, or -1 if it is
	 * currently disabled (i.e, should not display).
	 * 
	 * @return
	 */
	
	public int getNumButtons() {
		return !display ? -1 : currButtons.size();
	}
	
	public int getCodeAtPosition(int i) {
		return currButtons.get(i).getCode();
	}
	
	public String getImagePathAtPosition(int i) {
		return currButtons.get(i).getImagePath();
	}
	
	public String getLabelAtPosition(int i) {
		return currButtons.get(i).getLabel();
	}
	
	public HashSet<Integer> getCodes(){
		HashSet<Integer> out = new HashSet<Integer>();
		
		for(CorkboardButton cb : currButtons) {
			out.add(cb.getCode());
		}
		return out;
	}
	
	
}
