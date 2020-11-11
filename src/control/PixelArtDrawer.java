package control;

import manager.Manager;
import visual.View;

/**
 * 
 * Key input is universal and redirected to one location (keyboard shortcuts not dependent on context (mostly))
 * 
 * @author Ada Clevinger
 *
 */

public class PixelArtDrawer {

	private final static String IMAGE_NAME = "new_image";
	private final static String TEXT_WIDTH_REQUEST = "Please provide the width of the new image.";
	private final static String TEXT_HEIGHT_REQUEST = "Please provide the height of the new image.";
	
	private int counter;
	
	private View view;
	
	private Manager manager;
	
	public PixelArtDrawer() {
		manager = new Manager();
		view = new View(this);
	}
	
	/**
	 * Set up Communication static String access
	 * 
	 * @param in
	 */
	
	public void interpretCode(int in) {
			switch(in) {
				case CodeReference.CODE_NEW_PICTURE:
					manager.makeNewPicture(IMAGE_NAME + "_" + counter++, view.requestIntInput(TEXT_WIDTH_REQUEST), view.requestIntInput(TEXT_HEIGHT_REQUEST));
					break;
				case CodeReference.CODE_NEW_ANIMATION:
					break;
				case CodeReference.CODE_OPEN_FILE:
					break;
				case CodeReference.CODE_SAVE_THING:
					break;
				case CodeReference.CODE_SAVE_AS:
					break;
				case CodeReference.CODE_OPEN_META:
					break;
				case CodeReference.CODE_EXIT:
					break;
		}
	}
	
	public void interpretDraw(int x, int y, String nom) {
		
	}

}
