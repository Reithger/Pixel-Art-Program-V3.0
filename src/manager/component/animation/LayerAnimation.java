package manager.component.animation;

import java.awt.Image;
import java.util.ArrayList;

public class LayerAnimation {

	private ArrayList<ArtAnimation> animLayers;
	private boolean changes;
	
	//TODO generate animation using specific range of layers
	
	public LayerAnimation() {
		changes = true;
		animLayers = new ArrayList<ArtAnimation>();
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
