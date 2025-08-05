package manager.pen.drawing;

import java.awt.Color;
import java.util.HashMap;

import manager.pen.changes.Change;
import misc.Canvas;

public class Overlay {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static String REF_PASTE = "ref_paste";
	public final static String REF_SELECT_ORIGIN = "ref_select_origin";
	public final static String REF_SELECT_BORDER = "ref_select_border";
	public final static String REF_CHECKERBOARD = "ref_checkerboard";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, Change> instruct;
	private Canvas can;
	
	private int baseWid;
	private int baseHei;
	private int zoom;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Overlay(int wid, int hei, int inZoom) {
		can = new Canvas(wid * inZoom, hei * inZoom);
		zoom = inZoom;
		baseWid = wid;
		baseHei = hei;
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
	
	public void updateZoom(int inZoom) {
		System.out.println("Z: " + inZoom);
		if(zoom != inZoom) {
			zoom = inZoom;
			can.updateCanvasSize(baseWid * zoom, baseHei * zoom);
			initializeCanvas();
			instruct = new HashMap<String, Change>();
		}
	}
	
	public void initializeCanvas() {
		for(int i = 0; i < can.getCanvasWidth(); i++) {
			for(int j = 0; j < can.getCanvasHeight(); j++) {
				can.setCanvasColor(i, j, new Color(255, 255, 255, 0));
			}
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Canvas getCanvas() {
		return can;
	}

	public boolean containsOverlayFeature(String in) {
		return instruct.containsKey(in);
	}
	
}
