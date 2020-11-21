package manager.sketch;

import java.awt.Image;
import java.awt.image.BufferedImage;

import manager.curator.Curator;

public class SketchCanvas extends Sketch{

	public SketchCanvas(String nom, String ref) {
		super(nom, ref);
		setDrawable(true);
	}

	@Override
	public Sketch produceLayers(int layerSt, int layerEn) {
		SketchCanvas skCan = new SketchCanvas(getReference() + "_layer_" + layerSt + ":" + layerEn + "_" + counter++, getReference());
		skCan.setLayerStart(layerSt);
		skCan.setLayerEnd(layerEn);
		int act = getActiveLayer();
		if(act >= layerSt && act <= layerEn) {
			skCan.setActiveLayer(act);
		}
		else {
			skCan.setActiveLayer(layerSt);
		}
		return skCan;
	}

	@Override
	public BufferedImage[] getUpdateImages(Curator c) {
		BufferedImage underlay = null;
		BufferedImage overlay = null;
		if(getActiveLayer() > getLayerStart()) {
			underlay = c.getPictureImage(getReference(), getLayerStart(), getActiveLayer());
		}
		if(getActiveLayer() < getLayerEnd()) {
			overlay = c.getPictureImage(getReference(), getActiveLayer(), getLayerEnd() + 1);
		}
		return new BufferedImage[] {underlay, overlay};
	}

	@Override
	public Sketch copy() {
		SketchCanvas sk = new SketchCanvas(getName() + "(copy)", getReference());
		sk.setActiveLayer(getActiveLayer());
		sk.setLayerStart(getLayerStart());
		sk.setLayerEnd(getLayerEnd());
		sk.setZoom(getZoom());
		sk.setDrawable(getDrawable());
		sk.flagUpdate();
		return sk;
	}

}
