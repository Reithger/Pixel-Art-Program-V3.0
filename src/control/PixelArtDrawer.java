package control;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
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

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String TEXT_WIDTH_REQUEST = "Please provide the width of the new image.";
	private final static String TEXT_HEIGHT_REQUEST = "Please provide the height of the new image.";
	private final static String TEXT_CHOICE_PICTURE = "Picture";
	private final static String TEXT_CHOICE_ANIMATION = "Animation";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private View view;
	
	private Manager manager;
	
//---  Constructors   -------------------------------------------------------------------------
	
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
	
//---  Operations   ---------------------------------------------------------------------------

	//-- Receiving Code from View  ----------------------------
	
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
					removeThing(active);
					break;
				case CodeReference.CODE_DUPLICATE_THING:
					manager.duplicate(active);
					break;
				case CodeReference.CODE_OPEN_FILE:
					loadFile();
					break;
				case CodeReference.CODE_SAVE_THING:	//TODO: Check if thing already has default save location
					saveThing(active);
					break;
				case CodeReference.CODE_SAVE_AS:
					saveThingAs(active);
					break;
				case CodeReference.CODE_OPEN_META:
					//TODO: Plan out what the meta-settings menu should even do/keep track of
					break;
				case CodeReference.CODE_EXIT:
					if(view.requestConfirmation("Are you sure you want to exit?") && view.requestConfirmation("Would you like to save backups of your current work?")) {
						saveEverything();
					}
					System.exit(0);
					break;
		}
			updateView(false);
	}
	
	public void interpretDraw(int x, int y, String nom) {
		manager.drawToPicture(nom, x, y);
		updateView(false);
	}

	//-- Instructing Manager  ---------------------------------
	
	private void makeNewThing() {
		String choice = view.requestListChoice(new String[] {TEXT_CHOICE_PICTURE, TEXT_CHOICE_ANIMATION});
		switch(choice) {
			case TEXT_CHOICE_PICTURE:
				String nom = manager.getNewPictureName();
				String skNom = manager.makeNewPicture(nom, view.requestIntInput(TEXT_WIDTH_REQUEST), view.requestIntInput(TEXT_HEIGHT_REQUEST));
				addPicture(skNom, manager.getPictureImage(skNom), manager.getSketchDrawable(skNom));
				break;
			case TEXT_CHOICE_ANIMATION:
				break;
		}
	}

	private void saveEverything() {
		manager.saveAllBackup();
	}
	
	private void saveThing(String nom) {
		String path = manager.getDefaultFilePath(nom);
		if(path == null) {
			path = view.requestFolderPath("./", "Where do you want to save this to?");
		}
		manager.saveThing(nom, path, 1, true);
	}
	
	private void saveThingAs(String nom) {
		String path = manager.getDefaultFilePath(nom);
		path = view.requestFolderPath(path, "Where do you want to save this to?");
		String savNom = view.requestStringInput("What would you like to name the file?");
		manager.saveThing(nom, savNom, path, 1, true);
	}
	
	private void loadFile() {
		String path = getFilePath();
		//TODO: Either folder of images with a manifest on how to process them, or custom data type that the Manager can decode
		//TODO: For now, it will assume single layer images, will expand
		String nom = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
		String skNm = manager.loadInPicture(nom, path);
		addPicture(skNm, manager.getPictureImage(skNm), manager.getSketchDrawable(skNm));
	}
	
	private String getFilePath() {
		String path = view.requestFilePath("./", "Please select the file you want.");
		path = path.replaceAll("\\\\", "/");
		while(path.contains("//")) {
			path = path.replaceAll("//", "/");
		}
		return path;
	}
	
	//-- Updating View  ---------------------------------------
	
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
	
	public void updateThing(String nom, BufferedImage[] imgs, boolean drawable, int zoom) {
		view.updateDisplay(nom, imgs, drawable, zoom);
	}
	
	public void removeThing(String nom) {
		if(view.requestConfirmation("Are you sure you want to remove: " + nom + "?")) {
			manager.removeThing(nom);
			view.removeFromDisplay(nom);
		}
	}
	
	public void updateCanvasDisplay(String nom, int x, int y, Color[][] cols) {
		view.updateCanvasDisplay(nom, x, y, cols);
	}
	
	public void addAnimation(String nom, BufferedImage[] imgs, boolean drawable) {
		view.addAnimation(nom, imgs, drawable);
	}
	
	public void addPicture(String nom, BufferedImage img, boolean drawable) {
		view.addPicture(nom, img, drawable);
	}

}
