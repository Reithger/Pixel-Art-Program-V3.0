package manager.sketch;

import java.awt.Image;

import manager.curator.Curator;

public class SketchPicture extends Sketch{
	
	public SketchPicture(String nom, String ref) {
		super(nom, ref);
	}
	
	public SketchPicture(String nom, String ref, int laySt, int layEn, int layAc) {
		super(nom, ref);
		setLayerStart(laySt);
		setLayerEnd(layEn);
		setActiveLayer(layAc);
	}

	public Sketch produceLayers(int layerSt, int layerEn) {
		SketchPicture out = new SketchPicture(getName() + "_layer_" + layerSt + ":" + layerEn, getReference());
		out.setActiveLayer(layerSt);
		out.setLayerStart(layerSt);
		out.setLayerEnd(layerEn);
		return out;
	}

	public Image[] getUpdateImages(Curator c) {
		//TODO: More images returned if doing a translucent overlay or underlay
		return new Image[] {c.getPictureImage(getReference(), getLayerStart(), getLayerEnd())};
	}
	
}
