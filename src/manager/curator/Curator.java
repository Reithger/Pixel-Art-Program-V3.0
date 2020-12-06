package manager.curator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import manager.curator.animation.LayerAnimation;
import manager.curator.picture.LayerPicture;
import misc.Canvas;

public class Curator {

//---  Constants   ----------------------------------------------------------------------------

	private final static String SAVE_TYPE_PNG = "png";
	private final static String SAVE_TYPE_JPG = "jpg";
	private final static String DEFAULT_ANIMATION_SAVE_TYPE = "gif";
	private final static String DEFAULT_PICTURE_SAVE_TYPE = SAVE_TYPE_PNG;
	
	private final static String IMAGE_NAME = "new_image";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, LayerAnimation> animations;
	private HashMap<String, LayerPicture> pictures;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Curator() {
		animations = new HashMap<String, LayerAnimation>();
		pictures = new HashMap<String, LayerPicture>();
	}
	
//---  Operations   ---------------------------------------------------------------------------

	public void toggleUpdated(String nom) {
		if(animations.get(nom) != null) {
			animations.get(nom).designateUpdate();
		}
		else if(pictures.get(nom) != null) {
			pictures.get(nom).designateUpdate();
		}
	}
	
	public void resolveChanges() {
		for(LayerAnimation a : animations.values()) {
			a.resolvedUpdate();
		}
		for(LayerPicture p : pictures.values()) {
			p.resolvedUpdate();
		}
	}
	
	public void saveAllBackup() {
		int counter = 0;
		File f = new File("./backup");
		f.mkdir();
		for(String s : animations.keySet()) {
			animations.get(s).export(f.getAbsolutePath(), "backup_" + counter++, DEFAULT_ANIMATION_SAVE_TYPE, 1, true);
		}
		for(String s : pictures.keySet()) {
			pictures.get(s).export(f.getAbsolutePath(), "backup_" + counter++, DEFAULT_PICTURE_SAVE_TYPE, 1, true);
		}
	}
	
	//-- Things  ----------------------------------------------
	
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
	
	public void saveThing(String name, String path, int scale, boolean composite) {
		getComponent(name).export(path, name, SAVE_TYPE_PNG, scale, composite);
	}
	
	public void saveThing(String name, String savName, String path, int scale, boolean composite) {
		getComponent(name).export(path, savName, SAVE_TYPE_PNG, scale, composite);
	}
	
	//-- Animation Display  -------------------------------------------------------------------
	
	//-- Picture Display  ---------------------------------------------------------------------

	
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

	public BufferedImage producePictureLayer(String name, int layer) {
		return pictures.get(name).getLayer(layer).generateImage();
	}
	
	public BufferedImage producePictureImage(String name) {
		return pictures.get(name).generateImage();
	}
	
	public BufferedImage produceLayeredPictureImage(String name, int layStart, int layEnd) {
		return pictures.get(name).generateImageSetLayers(layStart, layEnd);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getNumLayers(String nom) {
		return getComponent(nom).getNumLayers();
	}
	
	public Canvas getPictureCanvas(String reference, int lS, int lE, int zoom) {
		return pictures.get(reference).getCanvas(lS, lE).getCanvas(zoom);
	}
	
	public String getDefaultPath(String nom) {
		return getComponent(nom).getDefaultFilePath();
	}
	
	public Component getComponent(String nom) {
		if(pictures.get(nom) != null) {
			return pictures.get(nom);
		}
		if(animations.get(nom) != null) {
			return animations.get(nom);
		}
		return null;
	}
	
	public String getNextPictureName() {
		int base = 0;
		while(getComponent(IMAGE_NAME + "_" + base) != null) {
			base++;
		}
		return IMAGE_NAME + "_" + base;
	}
	
	public boolean getUpdateStatus(String nom) {
		if(pictures.get(nom) != null) {
			return pictures.get(nom).getUpdateStatus();
		}
		if(animations.get(nom) != null) {
			return animations.get(nom).getUpdateStatus();
		}
		return false;
	}
	
	public LayerPicture getLayerPicture(String nom) {
		return pictures.get(nom);
	}
	
	public BufferedImage getPictureImage(String nom, int layerSt, int layerEn) {
		return pictures.get(nom).generateImageSetLayers(layerSt, layerEn);
	}

	public Canvas[] getAnimationFrames(String nom, int layerSt, int layerEn) {
		return animations.get(nom).getCanvasImages();
	}
	
}
