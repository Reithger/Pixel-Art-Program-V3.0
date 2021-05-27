package manager.pen;

import java.util.HashMap;

import manager.pen.changes.Change;
import misc.Canvas;

public class Overlay {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static String REF_PASTE = "ref_paste";
	public final static String REF_SELECT_ORIGIN = "ref_select_origin";
	public final static String REF_SELECT_BORDER = "ref_select_border";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Change> instruct;
	private Canvas can;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Overlay(int wid, int hei) {
		can = new Canvas(wid, hei);
		can.setSubGridSizeMaximum(32);
		instruct = new HashMap<String, Change>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void instruct(String ref, Change apply) {
		if(instruct.get(ref) != null) {
			instruct.get(ref).apply(can);
		}
		Integer[][] cols = apply.getColors();
		if(cols == null) {
			return;
		}
		Change undo = new Change();
		undo.setOverwrite(false);
		int x = apply.getX();
		int y = apply.getY();
		for(int i = x; i < x + cols.length; i++) {
			for(int j = y; j < y + cols[i - x].length; j++) {
				if(i >= 0 && j >= 0 && i < can.getCanvasWidth() && j < can.getCanvasHeight() && cols[i - x][j - y] != null) {
					undo.addChange(i, j, can.getCanvasIntValue(i,  j));
				}
			}
		}
		instruct.put(ref, undo);
		apply.apply(can);
	}
	
	public void release(String ref) {
		if(instruct.get(ref) != null) {
			instruct.get(ref).apply(can);
			instruct.remove(ref);
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Canvas getCanvas() {
		return can;
	}
	
}
