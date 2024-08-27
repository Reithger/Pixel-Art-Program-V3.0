package visual.drawboard.corkboard;

import misc.Canvas;
import visual.CodeMetaAccess;

public class CorkboardGenerator {

	private static int[] DEFAULT_BUTTONS_CANVAS;
	private static int[] DEFAULT_BUTTONS_HEADER;
	private static CodeMetaAccess CODE_REFERENCE;
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void assignCodeAccess(CodeMetaAccess cma) {
		CODE_REFERENCE = cma;
		Corkboard.assignCodeReference(cma);
	}
	
	public static void assignCodeHeaders(int header, int resize) {
		Corkboard.assignHeaderCodes(header, resize);
	}
	
	public static void assignDefaultCanvasButtonConfig(int[] codes) {
		DEFAULT_BUTTONS_CANVAS = codes;
	}
	
	public static void assignDefaultHeaderButtonConfig(int[] codes) {
		DEFAULT_BUTTONS_HEADER = codes;
	}
	
	public static Corkboard generateDisplayPicture(String name, String panelName, Canvas in) {
		Corkboard out = new DisplayPicture(name, panelName, in);
		initializeButtons(out);
		return out;
	}
	
	public static Corkboard generateDisplayAnimation(String name, String panelName, Canvas[] frames) {
		Corkboard out = new DisplayAnimation(name, panelName, frames);
		initializeButtons(out);
		return out;
	}
	
	private static void initializeButtons(Corkboard in) {
		in.addButtonsCanvas(DEFAULT_BUTTONS_CANVAS, CODE_REFERENCE.getCodeImagePaths(DEFAULT_BUTTONS_CANVAS), CODE_REFERENCE.getCodeLabels(DEFAULT_BUTTONS_CANVAS));
		in.addButtonsHeader(DEFAULT_BUTTONS_HEADER, CODE_REFERENCE.getCodeImagePaths(DEFAULT_BUTTONS_HEADER), CODE_REFERENCE.getCodeLabels(DEFAULT_BUTTONS_HEADER));
		in.updatePanel();
	}

}
