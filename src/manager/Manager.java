package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import manager.curator.picture.LayerPicture;
import manager.curator.picture.LayerSeries;
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
	
	public void optimizeStorage() {
		//TODO: Figure out a way to only do this when it seems necessary, remove LayerSeries dependency, simplify?
		HashSet<String> visited = new HashSet<String>();
		for(Sketch s : sketches.values()) {
			if(!visited.contains(s.getReference())) {
				visited.add(s.getReference());
				HashMap<LayerSeries, HashSet<Integer>> mappings = new HashMap<LayerSeries, HashSet<Integer>>();
				for(Sketch t : sketches.values()) {
					if(t.getReference().equals(s.getReference())) {
						LayerSeries curr = new LayerSeries(t.getLayerStart(), t.getLayerEnd());
						if(mappings.get(curr) == null) {
							mappings.put(curr, new HashSet<Integer>());
						}
						mappings.get(curr).add(t.getZoom());
					}
				}
				curator.optimizeStorage(s.getReference(), mappings.keySet());
				for(LayerSeries l : mappings.keySet()) {
					curator.optimizeStorage(s.getReference(), l, mappings.get(l));
				}
			}
		}
	}
	
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
		Sketch k = getSketch(name);
		curator.addLayer(k.getReference());
		flagUpdate(k);
	}
	
	public void moveLayer(String name, int start, int end) {
		Sketch k = getSketch(name);
		curator.moveLayer(k.getReference(), start, end);
		flagUpdate(k);
	}
	
	public void removeLayer(String name, int layer) {
		Sketch k = getSketch(name);
		curator.removeLayer(k.getReference(), layer);
		flagUpdate(k);
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
		flagUpdate(s);
	}
	
	public void moveSketchActiveLayerDown(String sket) {
		Sketch s = getSketch(sket);
		if(s.getActiveLayer() - 1 >= 0) {
			s.setActiveLayer(s.getActiveLayer() - 1);
			if(s.getActiveLayer() < s.getLayerStart()) {
				s.setLayerStart(s.getActiveLayer());
			}
		}
		flagUpdate(s);
	}
	
	public void setSketchLayerValues(String sket, int lStart, int lActive, int lEnd) {
		Sketch k = sketches.get(sket);
		k.setLayerStart(lStart);
		k.setActiveLayer(lActive);
		k.setLayerEnd(lEnd);
		flagUpdate(k);
	}
	
	public void increaseZoom(String nom) {
		Sketch k = getSketch(nom);
		k.setZoom(k.getZoom() + 1);
		flagUpdate(k);
	}
	
	public void decreaseZoom(String nom) {
		Sketch k = getSketch(nom);
		k.setZoom(k.getZoom() - 1);
		flagUpdate(k);
	}
	
	private void flagUpdate(Sketch in) {
		optimizeStorage();
		in.flagUpdate();
	}

//---  Getter Methods   -----------------------------------------------------------------------

	//-- Pen  -------------------------------------------------
	
	public Pen getPen() {
		return pen;
	}
	
	//-- Curator  ---------------------------------------------
	
	public String getNextName() {
		return curator.getNextDefaultName();
	}
	
	public String getDefaultFilePath(String nom) {
		if(getSketch(nom) == null) {
			return null;
		}
		return curator.getDefaultPath(getSketch(nom).getReference());
	}
	
	public Canvas getPictureCanvas(String nom) {
		Sketch pic = getSketch(nom);
		return curator.getPictureCanvas(pic.getReference(), pic.getLayerStart(), pic.getLayerEnd(), pic.getZoom());
	}
	
	public Canvas[] getSketchImages(String nom) {
		Sketch ska = getSketch(nom);
		return ska.getUpdateImages(curator);
	}

	//-- Sketches  --------------------------------------------
	
	private Sketch getSketch(String nom) {
		return sketches.get(nom);
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

	public ArrayList<String> getSketchNames(boolean force){
		ArrayList<String> out = new ArrayList<String>();
		for(Sketch k : sketches.values()) {
			if(force || k.needsUpdate()) {
				k.releaseUpdate();
				curator.resolveChanges(k.getReference());
				out.add(k.getName());
			}
		}
		return out;
	}
	
}
