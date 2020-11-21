package manager.curator.animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import manager.curator.Component;

public class LayerAnimation implements Component{

	private ArrayList<ArtAnimation> animLayers;
	private boolean changes;
	private String defaultPath;
	
	//TODO generate animation using specific range of layers
	
	public LayerAnimation() {
		changes = true;
		animLayers = new ArrayList<ArtAnimation>();
	}
	
	public void export(String path, String name, String type, int scale, boolean composite) {
		
	}
	
	public String getDefaultFilePath() {
		return defaultPath;
	}
	
	public int getNumLayers() {
		return animLayers.size();
	}
	
	public boolean getUpdateStatus() {
		return changes;
	}
	
	public void designateUpdate() {
		changes = true;
	}
	
	public void resolvedUpdate() {
		changes = false;
	}
	
	public BufferedImage[] getImages() {
		return null; //TODO Do this
	}
	
}
