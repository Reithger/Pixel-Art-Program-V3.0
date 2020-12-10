package manager.sketch;

import manager.curator.Curator;
import misc.Canvas;

public class SketchPicture extends Sketch{

//---  Constructors   -------------------------------------------------------------------------
	
	public SketchPicture(String nom, String ref) {
		super(nom, ref);
	}
	
	public SketchPicture(String nom, String ref, int laySt, int layEn, int layAc) {
		super(nom, ref);
		setLayerStart(laySt);
		setLayerEnd(layEn);
		setActiveLayer(layAc);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public Sketch produceLayers(int layerSt, int layerEn) {
		SketchPicture out = new SketchPicture(getName() + "_layer_" + layerSt + ":" + layerEn, getReference());
		out.setActiveLayer(layerSt);
		out.setLayerStart(layerSt);
		out.setLayerEnd(layerEn);
		return out;
	}

	@Override
	public Sketch copy() {
		SketchPicture sk = new SketchPicture(getName() + "(copy)", getReference());
		sk.setActiveLayer(getActiveLayer());
		sk.setLayerStart(getLayerStart());
		sk.setLayerEnd(getLayerEnd());
		sk.setZoom(getZoom());
		sk.flagUpdate();
		return sk;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Canvas[] getUpdateImages(Curator c) {
		//TODO: Locally handle single image preparation of translucent under/overlay
		return new Canvas[] {c.getPictureCanvas(getReference(), getLayerStart(), getLayerEnd(), getZoom())};
	}

}
