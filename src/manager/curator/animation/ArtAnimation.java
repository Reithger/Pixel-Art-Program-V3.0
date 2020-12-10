package manager.curator.animation;

import java.util.ArrayList;

import manager.curator.picture.LayerPicture;

public class ArtAnimation implements Comparable<ArtAnimation>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<LayerPicture> frames;
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
		LayerPicture hold = frames.get(a);
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
	
	public void addAnimationFrame(LayerPicture p) {
		frames.add(p);
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public ArrayList<LayerPicture> getAnimationFrames(){
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
