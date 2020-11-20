package control;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

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
	
	private volatile int counter;
	
	private View view;
	
	private Manager manager;
	
	public PixelArtDrawer() {
		manager = new Manager();
		view = new View(this);
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateView(false);
			}
		}, 0, 1000 / 30);*/
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
				case CodeReference.CODE_INCREASE_ZOOM:
					manager.increaseZoom(active);
					break;
				case CodeReference.CODE_DECREASE_ZOOM:
					manager.decreaseZoom(active);
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
				String nom = IMAGE_NAME + "_" + counter++;
				manager.makeNewPicture(nom, view.requestIntInput(TEXT_WIDTH_REQUEST), view.requestIntInput(TEXT_HEIGHT_REQUEST));
				addPicture(nom, manager.getPictureImage(nom), manager.getSketchDrawable(nom));
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
		for(String nom : manager.getSketchNames(force)) {
			updateThing(nom, manager.getSketchImages(nom), manager.getSketchDrawable(nom), manager.getSketchZoom(nom));
		}
		manager.reservePen();
		for(String canvPic : manager.getDrawnChanges()) {
			updateCanvasDisplay(canvPic, manager.getCanvasChangeStartX(canvPic), manager.getCanvasChangeStartY(canvPic), manager.getCanvasChangeColors(canvPic));
		}
		manager.disposeChanges();
		manager.releasePen();
	}
	
	public void updateThing(String nom, Image[] imgs, boolean drawable, int zoom) {
		view.updateDisplay(nom, imgs, drawable, zoom);
	}
	
	public void addAnimation(String nom, Image[] imgs, boolean drawable) {
		view.addAnimation(nom, imgs, drawable);
	}
	
	public void addPicture(String nom, Image img, boolean drawable) {
		view.addPicture(nom, img, drawable);
	}
	
	public void updateCanvasDisplay(String nom, int x, int y, Color[][] cols) {
		view.updateCanvasDisplay(nom, x, y, cols);
	}
	
}
