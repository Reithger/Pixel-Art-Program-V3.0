package manager.sketch;

import manager.curator.Curator;
import misc.Canvas;

public class SketchAnimation extends Sketch{
	
//---  Constructors   -------------------------------------------------------------------------
	
	public SketchAnimation(String nom, String ref) {
		super(nom, ref);
	}
	
	public SketchAnimation(String nom, String ref, int laySt, int layEn) {
		super(nom, ref);
		setLayerStart(laySt);
		setLayerEnd(layEn);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public Sketch produceLayers(int layerSt, int layerEn) {
		Sketch sk = new SketchAnimation(getName(), getReference());
		sk.setLayerStart(layerSt);
		sk.setLayerEnd(layerEn);
		sk.setActiveLayer(layerSt);
		return sk;
	}

	@Override
	public Sketch copy() {
		SketchAnimation sk = new SketchAnimation(getName() + "(copy)", getReference());
		sk.setActiveLayer(getActiveLayer());
		sk.setLayerStart(getLayerStart());
		sk.setLayerEnd(getLayerEnd());
		sk.setZoom(getZoom());
		sk.flagUpdate();
		return sk;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Canvas[] getUpdateImages(Curator c) {
		return c.getAnimationFrames(getReference(), getLayerStart(), getLayerEnd());
	}

}
