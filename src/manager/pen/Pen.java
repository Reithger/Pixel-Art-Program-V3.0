package manager.pen;

import java.awt.Color;
import java.util.ArrayList;

import control.code.CodeReference;
import manager.curator.picture.LayerPicture;
import manager.pen.color.ColorManager;
import manager.pen.drawing.DrawingManager;
import misc.Canvas;

public class Pen {
	
	/*
	 * 
	 * 
	 	 * Eraser?
	 	 * 
		 * 
		 * Mirroring image - just do it, flip canvas x/y
		 * Arbitrary color[][] to draw patterns - borrow from copy region activeColor[][] storage, need to gate the drawing for larger images
		 * System clipboard access to print - Toolkit probably, make new Image/canvas out of it
		 * 
	 */
	
	/*
	 * 
	 * Superlayer marking
	 * Pattern drawing
	 * Shape drawing (arbitrary polygon)
	 * 
	 * Split this up a bit for the different modes, messy at the moment
	 * 
	 */
	
//---  Constants   ----------------------------------------------------------------------------

	/**
	 * 
	 * Code values are set up here to correspond to external code input values so that
	 * when querying the state of the Model, there is an existing standard to understand
	 * the values by.
	 * 
	 * That is, if the current pen mode is Draw, that code is the same as the one the user
	 * triggers to change the mode to Draw so that we can know what image/overlay text corresponds
	 * to the backend model's state; thus we can tell the user with consistent visual language
	 * what the current mode is.
	 * 
	 * Region Select/Apply are outliers which have sub-options from the RegionDraw class, so
	 * they are local values that shouldn't overlap with any other code values we use which
	 * defer to the value stored in RegionDraw when contextually appropriate.
	 * 
	 */

	public final static int PEN_MODE_DRAW = CodeReference.CODE_PEN_MODE_DRAW;
	public final static int PEN_MODE_MOVE_CANVAS = CodeReference.CODE_PEN_MODE_MOVE_CANVAS;
	public final static int PEN_MODE_COLOR_PICK = CodeReference.CODE_PEN_MODE_COLOR_PICK;
	public final static int PEN_MODE_FILL = CodeReference.CODE_PEN_MODE_FILL;
	public final static int PEN_MODE_REGION_SELECT = -1;
	public final static int PEN_MODE_REGION_APPLY = -2;

	public final static int REGION_MODE_OUTLINE = CodeReference.CODE_PEN_REGION_MODE_OUTLINE;
	public final static int REGION_MODE_FILL = CodeReference.CODE_PEN_REGION_MODE_FILL;
	public final static int REGION_MODE_COPY = CodeReference.CODE_PEN_REGION_MODE_COPY;
	public final static int REGION_MODE_PASTE = CodeReference.CODE_PEN_REGION_MODE_PASTE;
		
//---  Instance Variables   -------------------------------------------------------------------
	
	private ColorManager color;
	private DrawingManager drawControl;

//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		color = new ColorManager();
		drawControl = new DrawingManager();
		DrawingManager.assignPenModeCodes(PEN_MODE_DRAW, PEN_MODE_MOVE_CANVAS, PEN_MODE_COLOR_PICK, PEN_MODE_FILL);
		DrawingManager.assignRegionModeCodes(REGION_MODE_OUTLINE, REGION_MODE_FILL, REGION_MODE_COPY, REGION_MODE_PASTE);
		drawControl.setPenMode(PEN_MODE_DRAW);
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void initializeCanvas(LayerPicture lP, int layer) {
		drawControl.initializeCanvas(lP, layer);
	}

	//-- StandardDraw  ----------------------------------------
	
	public boolean draw(String nom, LayerPicture lP, int layer, int x, int y, int duration) {
		if(getPenMode() == PEN_MODE_COLOR_PICK) {
			Integer nCol = lP.getColor(x, y, layer).getRGB();
			color.addColor(nCol);
			color.setColor(30);
			setPenMode(PEN_MODE_DRAW);
			return true;
		}
		else {
			return drawControl.draw(nom, lP, layer, x, y, duration, getActiveColor());
		}
	}

	public void toggleShading() {
		drawControl.toggleShading();
	}
	
	//-- Changes  ---------------------------------------------
	
	public void undo(String ref, LayerPicture lP) {
		drawControl.undo(ref,  lP);
	}
	
	public void redo(String ref, LayerPicture lP) {
		drawControl.redo(ref,  lP);
	}
	
	public void disposeChanges() {
		drawControl.disposeChanges();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void addColor(Color in) {
		color.addColor(in.getRGB());
	}
	
	public void removeColor(int index) {
		color.removeColor(index);
	}
	
	public void editColor(int index, Color col) {
		color.editColor(index, col.getRGB());
	}
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		color.editColor(index, chngR, chngG, chngB, chngA);
	}
	
	public void addPallet() {
		color.addPallet();
	}
	
	public void addPallet(ArrayList<String> cols) {
		ArrayList<Integer> co = new ArrayList<Integer>();
		for(String s : cols) {
			co.add(Integer.parseInt(s));
		}
		color.addPallet(co);
	}
	
	public void removePallet(int index) {
		color.removePallet(index);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setPenMode(int in) {
		drawControl.setPenMode(in);
	}
	
	public void enable() {
		drawControl.enable();
	}
	
	public void disable() {
		drawControl.disable();
	}
	
	//-- StandardDraw  ----------------------------------------
	
	public void setPenSize(int in) {
		drawControl.setPenSize(in);
	}
	
	public void setPenType(int index) {
		drawControl.setPenType(index);
	}
	
	public void setBlendQuotient(double in) {
		drawControl.setBlendQuotient(in);
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void setActiveColor(int ind) {
		color.setColor(ind);
	}
	
	public void setActiveColor(Color in) {
		color.setColor(in.getRGB());
	}

	public void setPallet(int in) {
		color.setPallet(in);
	}

	//-- RegionDraw  ------------------------------------------
	
	public void setRegionMode(int in) {
		drawControl.setRegionMode(in);
	}
	
	public void setRegionActiveSelect(int in) {
		drawControl.setRegionActiveSelect(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- StandardDraw  ----------------------------------------
	
	public Canvas getOverlay(String ref) {
		return drawControl.getOverlay(ref);
	}
	
	public int getPenSize() {
		return drawControl.getPenSize();
	}
	
	public double getBlendQuotient() {
		return drawControl.getBlendQuotient();
	}
	
	public int getPenType() {
		return drawControl.getPenType();
	}
	
	public int[] getPenDrawTypes() {
		return drawControl.getPenDrawTypes();
	}
	
	/**
	 * 
	 * This is relied on by DrawInstructions which needs to know that we are in a selection/application
	 * mode for a region on the pen, as well as by the Controller for updating the visuals.
	 * 
	 * @return
	 */
	
	public int getPenMode() {
		return drawControl.getPenMode();
	}
	
	public int getPenModeCode() {
		return drawControl.getPenModeCode();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public int getActiveColorValue() {
		return color.getActiveColor();
	}
	
	public Color getActiveColor() {
		return new Color(color.getActiveColor(), true);
	}
	
	public int getActiveColorIndex() {
		return color.getActiveColorIndex();
	}
	
	public ArrayList<Color> getColors(){
		ArrayList<Color> out = new ArrayList<Color>();
		for(Integer i : color.getColors()) {
			out.add(new Color(i, true));
		}
		return out;
	}
	
	public ArrayList<ArrayList<Integer>> getAllPallettes(){
		ArrayList<ArrayList<Integer>> out = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < color.getNumPallettes(); i++) {
			out.add(color.getPallette(i));
		}
		
		return out;
	}

	public int getCurrentPallet() {
		return color.getCurrentPalletIndex();
	}
	
	public int getCurrentPalletCodeBase() {
		return color.getCurrentPalletCodeBase();
	}
	
	public int getNumberPallettes() {
		return color.getNumPallettes();
	}
	
	//-- RegionDraw  ------------------------------------------
	
	public int getRegionMode() {
		return drawControl.getRegionMode();
	}
	
	public int getRegionActiveSelect() {
		return drawControl.getRegionActiveSelect();
	}
	
}
