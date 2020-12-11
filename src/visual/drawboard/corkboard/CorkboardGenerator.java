package visual.drawboard.corkboard;

import misc.Canvas;
import visual.drawboard.corkboard.buttons.ButtonManager;

public class CorkboardGenerator {

//---  Operations   ---------------------------------------------------------------------------
	
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
		ButtonManager.fillButtons(in);
		in.updatePanel();
	}

}
