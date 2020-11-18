package control;

import java.awt.Color;
import java.awt.Image;

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
	
	public void interpretCode(int in, String active) {
		System.out.println(in);
			switch(in) {
				case CodeReference.CODE_NEW_THING:
					makeNewThing();
					break;
				case CodeReference.CODE_CLOSE_THING:
					break;
				case CodeReference.CODE_DUPLICATE_THING:
					break;
				case CodeReference.CODE_OPEN_FILE:
					break;
				case CodeReference.CODE_SAVE_THING:
					manager.saveThing(active, view.requestFilePath("Where do you want to save this to?"), 1, true);
					break;
				case CodeReference.CODE_SAVE_AS:
					break;
				case CodeReference.CODE_OPEN_META:
					break;
				case CodeReference.CODE_EXIT:
					break;
		}
		updateView(false);
	}
	
	private void makeNewThing() {
		String choice = view.requestListChoice(new String[] {"Picture", "Animation"});
		switch(choice) {
			case "Picture":
				manager.makeNewPicture(IMAGE_NAME + "_" + counter++, view.requestIntInput(TEXT_WIDTH_REQUEST), view.requestIntInput(TEXT_HEIGHT_REQUEST));
				break;
			case "Animation":
				break;
		}
	}
	
	public void interpretDraw(int x, int y, String nom) {
		manager.drawToPicture(nom, x, y);
		updateView(false);
	}

	public void updateView(boolean force) {
		for(String dispAnim : manager.getSketchAnimationNames(force)) {
			updateAnimationDisplay(dispAnim, manager.getAnimationFrames(dispAnim));
		}
		for(String dispPic : manager.getSketchPictureNames(force)) {
			updatePictureDisplay(dispPic, manager.getPictureImage(dispPic));
		}
		manager.reservePen();
		for(String canvPic : manager.getSketchCanvasNames(force)) {
			updateCanvasDisplay(canvPic, manager.getCanvasChangeStartX(canvPic), manager.getCanvasChangeStartY(canvPic), manager.getCanvasChangeColors(canvPic));
		}
		manager.disposeChanges();
		manager.releasePen();
	}
	
	public void updateAnimationDisplay(String nom, Image[] imgs) {
		view.updateAnimationDisplay(nom, imgs);
	}
	
	public void updatePictureDisplay(String nom, Image img) {
		view.updatePictureDisplay(nom, img);
	}
	
	public void updateCanvasDisplay(String nom, int x, int y, Color[][] cols) {
		view.updateCanvasDisplay(nom, x, y, cols);
	}
	
}
