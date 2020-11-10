package manager;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import manager.component.animation.LayerAnimation;
import manager.component.picture.LayerPicture;

public class Manager {

//---  Constants   ----------------------------------------------------------------------------

	private final static int SAVE_TYPE_PNG = 0;
	private final static int SAVE_TYPE_JPG = 1;
	
	private HashMap<String, LayerAnimation> animations;
	private HashMap<String, LayerPicture> pictures;
	private Pen pen;
	
	public Manager() {
		animations = new HashMap<String, LayerAnimation>();
		pictures = new HashMap<String, LayerPicture>();
		pen = new Pen();
	}
	
	public void savePicture(String name, String path, int scale, boolean composite) {
		pictures.get(name).export(path, scale, composite);
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
	
	public void drawToPicture(String name, int layer, int x, int y) {
		pen.draw(pictures.get(name).getLayer(layer), x, y);
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
	
}
