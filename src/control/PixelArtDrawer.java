package control;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import manager.Manager;
import misc.Canvas;
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
		updateColors();
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
		if(checkRangeColors(in)) {
			int use = (in - CodeReference.CODE_RANGE_SELECT_COLOR - manager.getPen().getCurrentPalletCodeBase());
			manager.getPen().setActiveColor(use);
			updateColors();
		}
		else if(checkRangePenType(in)) {
			
		}
		else if(checkRangeLayers(in)) {
			
		}
		else {
			switch(in) {
			
				//-- File  ------------------------------------
			
				case CodeReference.CODE_NEW_THING:
					makeNewThing();
					break;
				case CodeReference.CODE_RENAME:
					if(active == null) {
						return;
					}
					String newName = view.requestStringInput("Please provide the new image name");
					HashMap<String, String> mappings = manager.rename(active, newName);
					view.rename(mappings);
					break;
	
				case CodeReference.CODE_CLOSE_THING:
					if(active == null) {
						return;
					}
					removeThing(active);
					break;
				case CodeReference.CODE_DUPLICATE_THING:
					if(active == null) {
						return;
					}
					view.duplicateThing(active, manager.duplicate(active));
					break;
				case CodeReference.CODE_OPEN_FILE:
					loadFile();
					break;
				case CodeReference.CODE_SAVE_THING:
					if(active == null) {
						return;
					}
					saveThing(active);
					break;
				case CodeReference.CODE_SAVE_AS:
					if(active == null) {
						return;
					}
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
					
				//-- Corkboard  -------------------------------
					
				case CodeReference.CODE_INCREASE_ZOOM:
					if(active == null) {
						return;
					}
					manager.increaseZoom(active);
					break;
				case CodeReference.CODE_DECREASE_ZOOM:
					if(active == null) {
						return;
					}
					manager.decreaseZoom(active);
					break;
					
				case CodeReference.CODE_ADD_LAYER:
					manager.addLayer(active);
					break;
				case CodeReference.CODE_REMOVE_LAYER:
					//TODO: Make custom popout for layer selection
					manager.removeLayer(active, view.requestIntInput("Which layer (#) do you want to remove?"));
					break;
				case CodeReference.CODE_MOVE_LAYER:
					manager.moveLayer(active, view.requestIntInput("Start Layer"), view.requestIntInput("End Layer"));
					break;
					
				//-- Drawing Stuff  ---------------------------
					
				case CodeReference.CODE_PEN_SIZE_INCREMENT:
					manager.getPen().incrementPenSize();
					break;
				case CodeReference.CODE_PEN_SIZE_DECREMENT:
					manager.getPen().decrementPenSize();
					break;
				case CodeReference.CODE_PEN_SIZE_SET:
					
					break;
				case CodeReference.CODE_COLOR_ADD:
					Random rand = new Random();
					Color nC = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
					manager.getPen().addColor(nC);
					updateColors();
					break;
			}
		}
		updateView(false);
	}
	
	private boolean checkRangeColors(int in) {
		return in >= CodeReference.CODE_RANGE_SELECT_COLOR && in < CodeReference.CODE_RANGE_SELECT_DRAW_TYPE;
	}
	
	private boolean checkRangePenType(int in) {
		return in >= CodeReference.CODE_RANGE_SELECT_DRAW_TYPE;
	}
	
	private boolean checkRangeLayers(int in) {
		return in >= CodeReference.CODE_RANGE_LAYER_SELECT && in < CodeReference.CODE_RANGE_SELECT_COLOR;
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
				addPicture(skNom, manager.getPictureCanvas(skNom));
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
		addPicture(skNm, manager.getPictureCanvas(skNm));
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
	
	private void updateColors() {
		view.updateColors(manager.getPen().getColors(), manager.getPen().getCurrentPalletCodeBase(), manager.getPen().getActiveColorIndex());
	}
	public void updateView(boolean force) {
		for(String nom : manager.getSketchNames(force)) {
			updateThing(nom, manager.getSketchImages(nom), manager.getSketchZoom(nom));
		}
	}
	
	public void updateThing(String nom, Canvas[] imgs, int zoom) {
		view.updateDisplay(nom, imgs, zoom);
	}
	
	public void removeThing(String nom) {
		if(view.requestConfirmation("Are you sure you want to remove: " + nom + "?")) {
			manager.removeThing(nom);
			view.removeFromDisplay(nom);
		}
	}

	public void addAnimation(String nom, Canvas[] imgs) {
		view.addAnimation(nom, imgs);
	}
	
	public void addPicture(String nom, Canvas img) {
		view.addPicture(nom, img);
	}

}
