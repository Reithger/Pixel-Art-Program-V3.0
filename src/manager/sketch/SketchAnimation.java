package manager.sketch;

import java.awt.Image;
import java.awt.image.BufferedImage;

import manager.curator.Curator;

public class SketchAnimation extends Sketch{
	
	private int activeFrame;	//TODO: Controls for how it should be configured
	private int speed;
	
	public SketchAnimation(String nom, String ref) {
		super(nom, ref);
	}
	
	public SketchAnimation(String nom, String ref, int laySt, int layEn) {
		super(nom, ref);
		setLayerStart(laySt);
		setLayerEnd(layEn);
	}
	
	public Sketch produceLayers(int layerSt, int layerEn) {
		Sketch sk = new SketchAnimation(getName(), getReference());
		sk.setLayerStart(layerSt);
		sk.setLayerEnd(layerEn);
		sk.setActiveLayer(layerSt);
		return sk;
	}
	
	public BufferedImage[] getUpdateImages(Curator c) {
		return c.getAnimationFrames(getReference(), getLayerStart(), getLayerEnd());
	}

	@Override
	public Sketch copy() {
		SketchAnimation sk = new SketchAnimation(getName() + "(copy)", getReference());
		sk.setActiveLayer(getActiveLayer());
		sk.setLayerStart(getLayerStart());
		sk.setLayerEnd(getLayerEnd());
		sk.setZoom(getZoom());
		sk.setDrawable(getDrawable());
		sk.flagUpdate();
		return sk;
	}
	
}
