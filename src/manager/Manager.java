package manager;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import manager.curator.picture.LayerPicture;
import manager.curator.Curator;
import manager.sketch.Sketch;
import manager.sketch.SketchCanvas;
import manager.sketch.SketchPicture;

public class Manager {

//---  Instance Variables   -------------------------------------------------------------------
	
	private Pen pen;
	private Curator curator;
	private HashMap<String, Sketch> sketches;
	
	private int counter;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Manager() {
		pen = new Pen();
		curator = new Curator();
		sketches = new HashMap<String, Sketch>();
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
		Sketch use = sketches.get(name);
		curator.saveThing(use.getReference(), path, scale, composite);
	}
	
	public void increaseZoom(String nom) {
		Sketch k = sketches.get(nom);
		k.setZoom(k.getZoom() + 1);
	}
	
	public void decreaseZoom(String nom) {
		Sketch k = sketches.get(nom);
		k.setZoom(k.getZoom() - 1);
	}
	
	//-- Animations  ------------------------------------------
	
	//-- Pictures  --------------------------------------------

	
	public void makeNewPicture(String name, int wid, int hei) {
		curator.makeNewPicture(name, wid, hei);
		SketchCanvas pic = new SketchCanvas(name, name);
		sketches.put(name, pic);
		pen.initializeCanvas(name, curator.getLayerPicture(name).getLayer(pic.getActiveLayer()));
	}
	
	public void loadInPicture(String name, String path) {
		curator.loadInPicture(name, path);
		String sketchName = getNextSketchName(name);
		SketchPicture skPic = new SketchPicture(sketchName, name);
		sketches.put(sketchName, skPic);
	}
	
	public void pullPictureLayers(String name, int layerSt, int layerEn) {
		Sketch skCan = sketches.get(name).produceLayers(layerSt, layerEn);
		sketches.put(skCan.getName(), skCan);
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
		Sketch spic = sketches.get(name);
		LayerPicture pic = curator.getLayerPicture(spic.getReference());
		pen.draw(name, pic.getLayer(spic.getActiveLayer()), x / spic.getZoom(), y / spic.getZoom());
		curator.toggleUpdated(spic.getReference());
	}
	
	public void disposeChanges() {
		pen.disposeChanges();
		curator.resolveChanges();
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getSketchZoom(String nom) {
		return sketches.get(nom).getZoom();
	}
	
	private String getNextSketchName(String base) {
		return base + "_" + counter++;
	}
	
	public boolean getSketchDrawable(String nom) {
		return sketches.get(nom).getDrawable();
	}
	
	public Image[] getSketchImages(String nom) {
		Sketch ska = sketches.get(nom);	//TODO Sketch can subsection this
		return ska.getUpdateImages(curator);
	}
	
	public Image getPictureImage(String nom) {
		Sketch pic = sketches.get(nom);
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
	
	public Sketch getSketch(String nom) {
		return sketches.get(nom);
	}
	
	public ArrayList<String> getSketchNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(Sketch k : sketches.values()) {
			if(force || k.needsUpdate() || curator.getUpdateStatus(k.getReference())) {
				k.releaseUpdate();
				out.add(k.getName());
			}
		}
		return out;
	}
	
	public ArrayList<String> getDrawnChanges(){
		return pen.getChangeNames();
	}
	
}
