package manager.sketch;

import java.awt.Image;
import java.awt.image.BufferedImage;

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

	public BufferedImage[] getUpdateImages(Curator c) {
		//TODO: More images returned if doing a translucent overlay or underlay
		return new BufferedImage[] {c.getPictureImage(getReference(), getLayerStart(), getLayerEnd())};
	}

	@Override
	public Sketch copy() {
		SketchPicture sk = new SketchPicture(getName() + "(copy)", getReference());
		sk.setActiveLayer(getActiveLayer());
		sk.setLayerStart(getLayerStart());
		sk.setLayerEnd(getLayerEnd());
		sk.setZoom(getZoom());
		sk.setDrawable(getDrawable());
		sk.flagUpdate();
		return sk;
	}
	
}
