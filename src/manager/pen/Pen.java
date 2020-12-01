package manager.pen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import manager.curator.picture.LayerPicture;

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
	
	//This needs to work as a queue of inverse actions to backtrack over for an undo function
	private volatile HashMap<String, LinkedList<Changes>> changes;	//TODO: Make this its own class
	private ColorManager color;
	private StandardDraw pencil;
	private volatile boolean mutex;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Pen() {
		mutex = false;
		changes = new HashMap<String, LinkedList<Changes>>();
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
				lP.setPixel(i, j, Color.white, layer);
			}
		}
		closeLock();
	}

	//-- StandardDraw  ----------------------------------------
	
	public void draw(String nom, LayerPicture lP, int layer, int x, int y) {
		openLock();
		//Changes c = new Changes(nom, layer);	//TODO: Time it so a Change is finalized after ~.25 seconds of inactivity
		pencil.draw(lP, x, y, layer, color.getActiveColor());	//TODO: integrate change details into drawing
		closeLock();
	}

	public void toggleShading() {
		pencil.toggleShading();
	}
	
	//-- Changes  ---------------------------------------------
	
	public void undo(String ref, LayerPicture lP) {
		if(changes.get(ref) != null) {
			Changes c = changes.get(ref).poll();
			//TODO:
		}
	}
	
	public void disposeChanges() {
		changes = new HashMap<String, LinkedList<Changes>>();
	}
	
	//-- ColorManager  ----------------------------------------
	
	public void editColor(int index, int chngR, int chngG, int chngB, int chngA) {
		color.editColor(index, chngR, chngG, chngB, chngA);
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
	
	public void addColor(Color in) {
		color.addColor(in);
	}
	
	public void removeColor(int index) {
		color.removeColor(index);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- StandardDraw  ----------------------------------------
	
	public int getPenSize() {
		return pencil.getPenSize();
	}
	
	public double getBlendQuotient() {
		return pencil.getBlendQuotient();
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

}
