package manager;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import manager.component.picture.LayerPicture;

public class Manager {

//---  Instance Variables   -------------------------------------------------------------------
	
	private Pen pen;
	private Curator curator;
	private HashMap<String, SketchPicture> pictures;
	private HashMap<String, SketchAnimation> animations;
	private HashMap<String, SketchCanvas> canvases;
	
	private int counter;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Manager() {
		pen = new Pen();
		curator = new Curator();
		pictures = new HashMap<String, SketchPicture>();
		animations = new HashMap<String, SketchAnimation>();
		canvases = new HashMap<String, SketchCanvas>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void reservePen() {
		pen.openLock();
	}
	
	public void releasePen() {
		pen.closeLock();
	}
	
	//-- Animations  --------------------------------------------------------------------------
	
	//-- Pictures  ----------------------------------------------------------------------------
	
	public void savePicture(String name, String path, int scale, boolean composite) {
		savePicture(name, path, scale, composite);
	}
	
	public void makeNewPicture(String name, int wid, int hei) {
		curator.makeNewPicture(name, wid, hei);
		String sketchName = getNextSketchName(name);
		SketchCanvas pic = new SketchCanvas(sketchName, name);
		canvases.put(sketchName, pic);
		pen.initializeCanvas(sketchName, curator.getLayerPicture(name).getLayer(pic.getActiveLayer()));
	}
	
	public void loadInPicture(String name, String path) {
		loadInPicture(name, path);
	}
	
	public void addLayer(String name) {
		addLayer(name);
	}
	
	public void moveLayer(String name, int start, int end) {
		moveLayer(name, start, end);
	}
	
	public void removeLayer(String name, int layer) {
		removeLayer(name, layer);
	}

	//-- Drawing  -----------------------------------------------------------------------------
	
	public void drawToPicture(String name, int x, int y) {
		SketchCanvas spic = canvases.get(name);
		LayerPicture pic = curator.getLayerPicture(spic.getReference());
		curator.toggleUpdated(spic.getReference());
		pen.draw(name, pic.getLayer(spic.getActiveLayer()), x, y);
	}
	
	public void disposeChanges() {
		pen.disposeChanges();
		curator.resolveChanges();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	private String getNextSketchName(String base) {
		return base + "_" + counter++;
	}
	
	public Image[] getAnimationFrames(String nom) {
		SketchAnimation ska = animations.get(nom);	//TODO Sketch can subsection this
		return curator.getAnimationFrames(ska.getReference(), ska.getLayerStart(), ska.getLayerEnd());
	}
	
	public Image getPictureImage(String nom) {
		SketchPicture pic = pictures.get(nom);
		return curator.getPictureImage(pic.getReference(), pic.getLayerStart(), pic.getLayerEnd());
	}
	
	public int getCanvasChangeStartX(String nom) {
		return pen.getChangeX(nom);
	}
	
	public int getCanvasChangeStartY(String nom) {
		return pen.getChangeY(nom);
	}
	
	public Color[][] getCanvasChangeColors(String nom){
		return pen.getChangeColors(nom);
	}
	
	public ArrayList<String> getSketchAnimationNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(String s : animations.keySet()) {
			if(force || curator.getUpdateStatus(animations.get(s).getReference()))
				out.add(s);
		}
		return out;
	}
	
	public ArrayList<String> getSketchPictureNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(String s : pictures.keySet()) {
			if(force || curator.getUpdateStatus(pictures.get(s).getReference()))
				out.add(s);
		}
		return out;
	}
	
	public ArrayList<String> getSketchCanvasNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(String s : canvases.keySet()) {
			if(force || curator.getUpdateStatus(canvases.get(s).getReference()))
				out.add(s);
		}
		return out;
	}
	
}
