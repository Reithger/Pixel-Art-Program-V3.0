package manager.sketch;

import java.awt.Image;

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
	public Image[] getUpdateImages(Curator c) {
		Image underlay = null;
		Image overlay = null;
		if(getActiveLayer() > getLayerStart()) {
			underlay = c.getPictureImage(getReference(), getLayerStart(), getActiveLayer());
		}
		if(getActiveLayer() < getLayerEnd()) {
			overlay = c.getPictureImage(getReference(), getActiveLayer(), getLayerEnd() + 1);
		}
		return new Image[] {underlay, overlay};
	}

}
