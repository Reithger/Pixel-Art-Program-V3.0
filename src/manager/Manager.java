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
	
	//-- Things  ----------------------------------------------
	
	public void saveThing(String name, String path, int scale, boolean composite) {
		curator.saveThing(name, path, scale, composite);
	}
	//-- Animations  ------------------------------------------
	
	//-- Pictures  --------------------------------------------

	
	public void makeNewPicture(String name, int wid, int hei) {
		curator.makeNewPicture(name, wid, hei);
		String sketchName = getNextSketchName(name);
		SketchCanvas pic = new SketchCanvas(sketchName, name);
		canvases.put(sketchName, pic);
		pen.initializeCanvas(sketchName, curator.getLayerPicture(name).getLayer(pic.getActiveLayer()));
	}
	
	public void loadInPicture(String name, String path) {
		curator.loadInPicture(name, path);
		String sketchName = getNextSketchName(name);
		SketchPicture skPic = new SketchPicture(sketchName, name);
		pictures.put(sketchName, skPic);
	}
	
	public void pullCanvasLayer(String name, int layer) {
		SketchPicture skPic = pictures.get(name);
		SketchCanvas skC = skPic.produceCanvasLayer(layer);
		canvases.put(skC.getName(), skC);
	}
	
	public void addLayer(String name) {
		curator.addLayer(name);
	}
	
	public void moveLayer(String name, int start, int end) {
		curator.moveLayer(name, start, end);
	}
	
	public void removeLayer(String name, int layer) {
		curator.removeLayer(name, layer);
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
