package manager.component.animation;

import java.util.ArrayList;

import manager.component.Component;
import manager.component.picture.ArtPicture;

public class ArtAnimation implements Component, Comparable<ArtAnimation>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<ArtPicture> frames;
	private int layer;
	
//---  Constructors   -------------------------------------------------------------------------

//---  Operations   ---------------------------------------------------------------------------
	
	public void saveAnimation(String filePath, int type) {
		//TODO:
	}
	
	public void export(String filePath, String saveType) {	//TODO: Options to save as gif, apng, and always as folder of images
		//TODO: encode so can be read back in; might just be generating a folder of composite images?
	}
	
	public void importAnimation(String filePath) {
		//TODO: Read encoded data to generate Animation
	}
	
	public void moveFramePosition(int a, int b) {
		ArtPicture hold = frames.get(a);
		frames.remove(a);
		frames.add(b > a ? b - 1 : b, hold);
	}
	
	public void setLayer(int lay) {
		layer = lay;
	}
	
	//-- Animation Frames  ------------------------------------
	
	public void removeAnimationFrame(int ind) {
		frames.remove(ind);
	}
	
	public void addAnimationFrame(ArtPicture p) {
		frames.add(p);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public ArrayList<ArtPicture> getAnimationFrames(){
		return frames;
	}

	public int getLayer() {
		return layer;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int compareTo(ArtAnimation o) {
		int l = o.getLayer();
		if(getLayer() > l) {
			return 1;
		}
		else if(getLayer() < l) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
