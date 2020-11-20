package manager.curator.animation;

import java.awt.Image;
import java.util.ArrayList;

import manager.curator.Component;

public class LayerAnimation implements Component{

	private ArrayList<ArtAnimation> animLayers;
	private boolean changes;
	
	//TODO generate animation using specific range of layers
	
	public LayerAnimation() {
		changes = true;
		animLayers = new ArrayList<ArtAnimation>();
	}
	
	public void export(String path, String type, int scale, boolean composite) {
		
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
	
	public Image[] getImages() {
		return null; //TODO Do this
	}
	
}
