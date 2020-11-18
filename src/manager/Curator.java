package manager;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import manager.component.animation.LayerAnimation;
import manager.component.picture.LayerPicture;

public class Curator {

//---  Constants   ----------------------------------------------------------------------------

	private final static int SAVE_TYPE_PNG = 0;
	private final static int SAVE_TYPE_JPG = 1;
	
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
	
	//-- Things  ----------------------------------------------
	
	public void saveThing(String name, String path, int scale, boolean composite) {
		if(pictures.get(name) != null) {
			pictures.get(name).export(path, scale, composite);
		}
		else if(animations.get(name) != null) {
			animations.get(name).export(path, scale, composite);
		}
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
	
	public Image producePictureImage(String name) {
		return pictures.get(name).generateImage();
	}
	
	public Image produceLayeredPictureImage(String name, int layStart, int layEnd) {
		return pictures.get(name).generateImageSetLayers(layStart, layEnd);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
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
	
	public Image getPictureImage(String nom, int layerSt, int layerEn) {
		return pictures.get(nom).generateImageSetLayers(layerSt, layerEn);
	}

	public Image[] getAnimationFrames(String nom, int layerSt, int layerEn) {
		return animations.get(nom).getImages();
	}
	
}
