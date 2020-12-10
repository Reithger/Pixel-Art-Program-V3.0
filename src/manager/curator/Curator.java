package manager.curator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import manager.curator.animation.LayerAnimation;
import manager.curator.picture.LayerPicture;
import manager.curator.picture.LayerSeries;
import misc.Canvas;

public class Curator {

//---  Constants   ----------------------------------------------------------------------------

	private final static String SAVE_TYPE_PNG = "png";
	private final static String SAVE_TYPE_JPG = "jpg";
	private final static String DEFAULT_ANIMATION_SAVE_TYPE = "gif";
	private final static String DEFAULT_PICTURE_SAVE_TYPE = SAVE_TYPE_PNG;
	
	private final static String DEFAULT_NAME = "new_art";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, LayerAnimation> animations;
	private HashMap<String, LayerPicture> pictures;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Curator() {
		animations = new HashMap<String, LayerAnimation>();
		pictures = new HashMap<String, LayerPicture>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	//-- Component  -------------------------------------------
	
	public void toggleUpdated(String nom) {
		getComponent(nom).designateUpdate();
	}
	
	public void resolveChanges(String nom) {
		getComponent(nom).resolveUpdate();
	}
	
	public void saveAllBackup() {
		int counter = 0;
		File f = new File("./backup");
		f.mkdir();
		for(String s : getNames()) {
			getComponent(s).export(f.getAbsolutePath(), "backup_" + counter++, DEFAULT_ANIMATION_SAVE_TYPE, 1, true);
		}
	}
	
	public void saveThing(String name, String path, int scale, boolean composite) {
		getComponent(name).export(path, name, DEFAULT_PICTURE_SAVE_TYPE, scale, composite);
	}
	
	public void saveThing(String name, String savName, String path, int scale, boolean composite) {
		getComponent(name).export(path, savName, DEFAULT_PICTURE_SAVE_TYPE, scale, composite);
	}
	
	//-- Universal  -------------------------------------------
	
	public void rename(String ref, String newName) {
		if(animations.get(ref) != null) {
			LayerAnimation lA = animations.get(ref);
			animations.remove(ref);
			animations.put(newName, lA);
		}
		if(pictures.get(ref) != null) {
			LayerPicture lP = pictures.get(ref);
			pictures.remove(ref);
			pictures.put(newName, lP);
		}
	}

	//-- Animation Display  -----------------------------------
	
	//-- Picture Display  -------------------------------------

	public void optimizeStorage(String ref, Collection<LayerSeries> lS) {
		HashSet<LayerSeries> use = new HashSet<LayerSeries>();
		for(LayerSeries l : lS) {
			use.add(l);
		}
		pictures.get(ref).optimizeStorage(use);
	}
	
	public void optimizeStorage(String ref, LayerSeries lS, HashSet<Integer> zooms) {
		pictures.get(ref).optimizeStorage(lS, zooms);
	}
	
	public void makeNewPicture(String name, int wid, int hei) {
		LayerPicture lP = new LayerPicture(wid, hei);
		lP.addLayer();
		pictures.put(name, lP);
	}
	
	public void loadInPicture(String name, String path) {
		LayerPicture lP = new LayerPicture(path);
		pictures.put(name, lP);
	}
	
	public void addLayer(String name) {
		pictures.get(name).addLayer();
	}
	
	public void moveLayer(String name, int start, int end) {
		pictures.get(name).moveLayers(start, end);
	}
	
	public void removeLayer(String name, int layer) {
		pictures.get(name).removeLayer(layer);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	//-- Components  ------------------------------------------
	
	private ArrayList<String> getNames(){
		ArrayList<String> out = new ArrayList<String>();
		out.addAll(pictures.keySet());
		out.addAll(animations.keySet());
		return out;
	}
	
	private Component getComponent(String nom) {
		if(pictures.get(nom) != null) {
			return pictures.get(nom);
		}
		if(animations.get(nom) != null) {
			return animations.get(nom);
		}
		return null;
	}
	
	public String getDefaultPath(String nom) {
		return getComponent(nom).getDefaultFilePath();
	}
	
	public boolean getUpdateStatus(String nom) {
		return getComponent(nom).getUpdateStatus();
	}
	
	public String getNextDefaultName() {
		int base = 0;
		while(getComponent(DEFAULT_NAME + "_" + base) != null) {
			base++;
		}
		return DEFAULT_NAME + "_" + base;
	}
	
	public int getNumLayers(String nom) {
		return getComponent(nom).getNumLayers();
	}

	//-- Picture  ---------------------------------------------

	public Canvas getPictureCanvas(String reference, int lS, int lE, int zoom) {
		return pictures.get(reference).getCanvas(lS, lE).getCanvas(zoom);
	}

	public LayerPicture getLayerPicture(String nom) {
		return pictures.get(nom);
	}
	
	//-- Animation  -------------------------------------------
	
	public Canvas[] getAnimationFrames(String nom, int layerSt, int layerEn) {
		return animations.get(nom).getCanvasImages();
	}
	
}
