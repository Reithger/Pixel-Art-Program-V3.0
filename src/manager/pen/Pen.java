package manager.pen;

import java.awt.Color;
import java.util.ArrayList;

import manager.curator.picture.LayerPicture;
import manager.pen.changes.Change;
import manager.pen.changes.VersionHistory;
import manager.pen.color.ColorManager;
import manager.pen.drawing.StandardDraw;

public class Pen {
	
	/*
	 * Select colors - reassign activeColor from stored list
	 * Make new colors - add/edit to stored list, casual interface 'qwaszx' rgb sliders, or pop-up with more fine tools
	 * Transparency - ^^^, part of all that!
	 * 
	 * Pen size - integer value, visual representation of current size with ^/v buttons, keyboard shortcut 'lk'
	 * Pen shape - constant array of choices to select from, different draw function for each
	 * Pen shading - boolean, blends current pen color with canvas pixels (different degrees)
	 * 
	 	 * Eraser?
	 	 * 
		 * Undo - invert Changes class, store as queue to undo gradually
		 * 
		 * Select mode - go into region select, after marking an area then pick contextual action vvv
			 * Copy region - boolean to initiate a region-select mode, integers for start position, activeColor[][] storage (multiple copied?)
			 * Outline region - ^^^, no activeColor[][] though just draw along edges
			 * Fill region - ^^^ but fill
		 *
		 * Color picker - boolean, next click adds grabbed color to color list/active color
		 * 
		 * Mirroring image - just do it, flip canvas x/y
		 * Arbitrary color[][] to draw patterns - borrow from copy region activeColor[][] storage, need to gate the drawing for larger images
		 * System clipboard access to print - Toolkit probably, make new Image/canvas out of it
		 * 
	 */
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private VersionHistory changes;
	private ColorManager color;
	private StandardDraw pencil;
	private volatile boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		mutex = false;
		changes = new VersionHistory();
		color = new ColorManager();
		pencil = new StandardDraw();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	private void openLock() {
		while(mutex) {}
		mutex = true;
	}
	
	private void closeLock() {
		mutex = false;
	}
	
	public void initializeCanvas(LayerPicture lP, int layer) {
		openLock();
		for(int i = 0; i < lP.getWidth(); i++) {
			for(int j = 0; j < lP.getHeight(); j++) {
				lP.setPixel(i, j, new Color(255, 255, 255, 0), layer);
			}
		}
		closeLock();
	}

	//-- StandardDraw  ----------------------------------------
	
	public void draw(String nom, LayerPicture lP, int layer, int x, int y, int duration) {
		openLock();
		Change[] change = pencil.draw(lP, x, y, layer, duration, color.getActiveColor());
		change[0].setName(nom);
		change[1].setName(nom);
		changes.addChange(nom, layer, duration, change[0], change[1]);
		closeLock();
	}

	public void toggleShading() {
		pencil.toggleShading();
	}
	
	//-- Changes  ---------------------------------------------
	
	public void undo(String ref, LayerPicture lP) {
		Change c = changes.getUndo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), c.getLayer());
		}
	}
	
	public void redo(String ref, LayerPicture lP) {
		Change c = changes.getRedo(ref);
		if(c != null) {
			lP.setRegion(c.getX(), c.getY(), c.getColors(), c.getLayer());
		}
	}
	
	public void disposeChanges() {
		changes = new VersionHistory();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void addColor(Color in) {
		color.addColor(in);
	}
	
	public void removeColor(int index) {
		color.removeColor(index);
	}
	
	public void editColor(int index, Color col) {
		color.editColor(index, col);
	}
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		color.editColor(index, chngR, chngG, chngB, chngA);
	}
	
	public void addPallet() {
		color.addPallet();
	}
	
	public void addPallet(ArrayList<Color> cols) {
		color.addPallet(cols);
	}
	
	public void removePallet(int index) {
		color.removePallet(index);
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	//-- StandardDraw  ----------------------------------------
	
	public void setPenSize(int in) {
		pencil.setPenSize(in);
	}
	
	public void incrementPenSize() {
		pencil.setPenSize(pencil.getPenSize() + 1);
	}
	
	public void decrementPenSize() {
		pencil.setPenSize(pencil.getPenSize() - 1);
	}
	
	public void setPenType(int index) {
		pencil.setPenDrawType(index);
	}
	
	public void setBlendQuotient(double in) {
		pencil.setBlendQuotient(in);
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void setActiveColor(int ind) {
		color.setColor(ind);
	}
	
	public void setActiveColor(Color in) {
		color.setColor(in);
	}

	public void setPallet(int in) {
		color.setPallet(in);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- StandardDraw  ----------------------------------------
	
	public int getPenSize() {
		return pencil.getPenSize();
	}
	
	public double getBlendQuotient() {
		return pencil.getBlendQuotient();
	}
	
	public int getPenType() {
		return pencil.getPenType();
	}
	
	public int[] getPenDrawTypes() {
		return pencil.getDrawTypes();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public Color getActiveColor() {
		return color.getActiveColor();
	}
	
	public int getActiveColorIndex() {
		return color.getActiveColorIndex();
	}
	
	public ArrayList<Color> getColors(){
		return color.getColors();
	}

	public int getCurrentPallet() {
		return color.getCurrentPalletIndex();
	}
	
	public int getCurrentPalletCodeBase() {
		return color.getCurrentPalletCodeBase();
	}
	
}
