package manager;

import java.util.ArrayList;
import java.util.HashMap;

import manager.curator.picture.LayerPicture;
import manager.pen.Pen;
import manager.curator.Curator;
import manager.sketch.Sketch;
import manager.sketch.SketchPicture;
import misc.Canvas;

public class Manager {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private Pen pen;
	private Curator curator;
	private HashMap<String, Sketch> sketches;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Manager() {
		pen = new Pen();
		curator = new Curator();
		sketches = new HashMap<String, Sketch>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void saveAllBackup() {
		curator.saveAllBackup();
	}
	
	//-- Pen  -------------------------------------------------
	
	public void undo(String ref) {
		Sketch s = getSketch(ref);
		pen.undo(s.getReference(), curator.getLayerPicture(s.getReference()));
	}
	
	public void redo(String ref) {
		Sketch s = getSketch(ref);
		pen.redo(s.getReference(), curator.getLayerPicture(s.getReference()));
	}
	
	public void drawToPicture(String name, int x, int y, int duration) {
		Sketch spic = sketches.get(name);
		int actX = x / spic.getZoom();
		int actY = y / spic.getZoom();
		if(actX < 0 || actY < 0) {
			return;
		}
		LayerPicture pic = curator.getLayerPicture(spic.getReference());
		pen.draw(spic.getReference(), pic, spic.getActiveLayer(), actX, actY, duration);
	}

	//-- Curator  ---------------------------------------------
	
		//-- Generic  -----------------------------------------
	
	public HashMap<String, String> rename(String old, String newName) {
		Sketch sk = sketches.get(old);
		String ref = sk.getReference();
		curator.rename(ref, newName);
		ArrayList<Sketch> addBack = new ArrayList<Sketch>();
		ArrayList<String> remove = new ArrayList<String>();
		HashMap<String, String> out = new HashMap<String, String>();
		int counter = 0;
		for(Sketch s : sketches.values()) {
			if(s.getReference().equals(ref)) {
				s.setReference(newName);
				remove.add(s.getName());
				out.put(s.getName(), newName + "_" + counter++);
				s.setName(out.get(s.getName()));
				addBack.add(s);
			}
		}
		
		for(String s : remove) {
			sketches.remove(s);
		}
		
		for(Sketch s : addBack) {
			sketches.put(s.getName(), s);
		}
		
		return out;
	}

	public void removeThing(String name) {
		sketches.remove(name);
	}
	
	public void saveThing(String name, String path, int scale, boolean composite) {
		Sketch use = sketches.get(name);
		curator.saveThing(use.getReference(), path, scale, composite);
	}
	
	public void saveThing(String name, String savNam, String path, int scale, boolean composite) {
		Sketch use = sketches.get(name);
		curator.saveThing(use.getReference(), savNam, path, scale, composite);
	}

	public void addLayer(String name) {
		curator.addLayer(getSketch(name).getReference());
		getSketch(name).flagUpdate();
	}
	
	public void moveLayer(String name, int start, int end) {
		curator.moveLayer(getSketch(name).getReference(), start, end);
		getSketch(name).flagUpdate();
	}
	
	public void removeLayer(String name, int layer) {
		curator.removeLayer(getSketch(name).getReference(), layer);
		getSketch(name).flagUpdate();
	}

	public String duplicate(String nom) {
		Sketch sk = getSketch(nom).copy();
		String skName = getNextSketchName(sk.getReference());
		sk.setName(skName);
		sketches.put(skName, sk);
		return skName;
	}
		
		//-- Animations  --------------------------------------
		
		//-- Pictures  ----------------------------------------

	public String makeNewPicture(String name, int wid, int hei) {
		curator.makeNewPicture(name, wid, hei);
		String sketchName = getNextSketchName(name);
		SketchPicture pic = new SketchPicture(sketchName, name);
		sketches.put(pic.getName(), pic);
		pen.initializeCanvas(curator.getLayerPicture(name), 0);
		return sketchName;
	}
	
	public String loadInPicture(String name, String path) {
		curator.loadInPicture(name, path);
		String sketchName = getNextSketchName(name);
		SketchPicture skPic = new SketchPicture(sketchName, name);
		sketches.put(skPic.getName(), skPic);
		return sketchName;
	}

	//-- Sketches  --------------------------------------------
	
	public void setSketchLayersAll(String sket) {
		Sketch s = getSketch(sket);
		int active = s.getActiveLayer();
		active = active < 0 ? 0 : active;
		int max = curator.getNumLayers(s.getReference());
		active = active >= max ? max - 1 : active;
		setSketchLayerValues(sket, 0, s.getActiveLayer(), max - 1);
	}
	
	public void setSketchLayersBeneath(String sket) {
		Sketch s = getSketch(sket);
		setSketchLayerValues(sket, 0, s.getActiveLayer(), s.getActiveLayer());
	}
	
	public void setSketchLayersActive(String sket) {
		Sketch s = getSketch(sket);
		setSketchLayerValues(sket, s.getActiveLayer(), s.getActiveLayer(), s.getActiveLayer());
	}
	
	public void moveSketchActiveLayerUp(String sket) {
		Sketch s = getSketch(sket);
		if(s.getActiveLayer() + 1 < curator.getNumLayers(s.getReference())) {
			s.setActiveLayer(s.getActiveLayer() + 1);
			if(s.getActiveLayer() > s.getLayerEnd()) {
				s.setLayerEnd(s.getActiveLayer());
			}
		}
		s.flagUpdate();
	}
	
	public void moveSketchActiveLayerDown(String sket) {
		Sketch s = getSketch(sket);
		if(s.getActiveLayer() - 1 >= 0) {
			s.setActiveLayer(s.getActiveLayer() - 1);
			if(s.getActiveLayer() < s.getLayerStart()) {
				s.setLayerStart(s.getActiveLayer());
			}
		}
		s.flagUpdate();
	}
	
	public void setSketchLayerValues(String sket, int lStart, int lActive, int lEnd) {
		Sketch k = sketches.get(sket);
		k.setLayerStart(lStart);
		k.setActiveLayer(lActive);
		k.setLayerEnd(lEnd);
		k.flagUpdate();
	}
	
	public void increaseZoom(String nom) {
		Sketch k = getSketch(nom);
		k.setZoom(k.getZoom() + 1);
		k.flagUpdate();
	}
	
	public void decreaseZoom(String nom) {
		Sketch k = getSketch(nom);
		k.setZoom(k.getZoom() - 1);
		k.flagUpdate();
	}

//---  Getter Methods   -----------------------------------------------------------------------

	public Pen getPen() {
		return pen;
	}
	
	private Sketch getSketch(String nom) {
		return sketches.get(nom);
	}
	
	public String getNewPictureName() {
		return curator.getNextPictureName();
	}
	
	public String getDefaultFilePath(String nom) {
		if(getSketch(nom) == null) {
			return null;
		}
		return curator.getDefaultPath(getSketch(nom).getReference());
	}
	
	public int getSketchZoom(String nom) {
		return getSketch(nom).getZoom();
	}
	
	private String getNextSketchName(String base) {
		int use = 0;
		while(sketches.get(base + "_" + use) != null) {
			use++;
		}
		return base + "_" + use;
	}
	
	public Canvas[] getSketchImages(String nom) {
		Sketch ska = getSketch(nom);
		return ska.getUpdateImages(curator);
	}
	
	public Canvas getPictureCanvas(String nom) {
		Sketch pic = getSketch(nom);
		return curator.getPictureCanvas(pic.getReference(), pic.getLayerStart(), pic.getLayerEnd(), pic.getZoom());
	}

	public ArrayList<String> getSketchNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(Sketch k : sketches.values()) {
			if(force || k.needsUpdate()) {
				k.releaseUpdate();
				out.add(k.getName());
			}
		}
		return out;
	}
	
}
