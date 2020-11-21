package visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import control.PixelArtDrawer;
import filemeta.FileChooser;
import visual.composite.popout.PopoutAlert;
import visual.composite.popout.PopoutSelectList;
import visual.drawboard.DrawingBoard;
import visual.frame.WindowFrame;
import visual.settings.SettingsBar;

public class View {
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 7;

//---  Instance Variables   -------------------------------------------------------------------
	
	private PixelArtDrawer reference;
	
	private WindowFrame frame;
	
	private SettingsBar options;
	
	private DrawingBoard body;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public View(PixelArtDrawer in) {
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setName("Test");
		options = new SettingsBar(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame, this);
		body = new DrawingBoard(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame, this);
		frame.reserveWindow(body.getWindowName());
		frame.showActiveWindow(body.getWindowName());
		reference = in;
	}

//---  Operations   ---------------------------------------------------------------------------
	
	//-- Input Handling  --------------------------------------
	
	public void handOffInt(int code) {
		reference.interpretCode(code, body.getActiveElement());
	}
	
	public void handOffClick(int x, int y, String nom) {
		reference.interpretDraw(x, y, nom);
	}

	//-- User Request  ----------------------------------------
	
	public int requestIntInput(String text) {
		PopoutInputRequest piR = new PopoutInputRequest(text);
		String out = piR.getSubmitted();
		piR.dispose();
		try {
			return Integer.parseInt(out);
		}
		catch(Exception e) {
			new PopoutAlert(300, 250, "Error in text entry: non-integer value provided");
			return requestIntInput(text);
		}
	}
	
	public String requestStringInput(String text) {
		PopoutInputRequest piR = new PopoutInputRequest(text);
		String out = piR.getSubmitted();
		piR.dispose();
		return out;
	}
	
	public String requestListChoice(String[] listIn) {
		PopoutSelectList psL = new PopoutSelectList(250, 200, listIn, false);
		String out = psL.getSelected();
		psL.dispose();
		return out;
	}
	
	public String requestFolderPath(String defDir, String display) {
		return FileChooser.promptSelectFile(defDir, true, false).toString();
	}
	
	public String requestFilePath(String defDir, String display) {
		return FileChooser.promptSelectFile(defDir, true, true).toString();
	}

	public boolean requestConfirmation(String display) {
		PopoutConfirm pC = new PopoutConfirm(200, 150, display);
		boolean out = pC.getChoice();
		pC.dispose();
		return out;
	}
	
	//-- Drawing Board Management  ----------------------------
	
	public void updateDisplay(String nom, BufferedImage[] imgs, boolean drawable, int zoom) {
		body.updateDisplay(nom, imgs, drawable, zoom);
	}
	
	public void removeFromDisplay(String nom) {
		body.removeFromDisplay(nom);
	}
	
	public void addPicture(String nom, BufferedImage img, boolean drawable) {
		body.addPicture(nom, img, drawable);
	}
	
	public void addAnimation(String nom, BufferedImage[] img, boolean drawable) {
		body.addAnimation(nom, img, drawable);
	}
	
	public void updateCanvasDisplay(String nom, int x, int y, Color[][] cols) {
		body.updatePictureCanvas(nom, x, y, cols);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getActiveElement() {
		return body.getActiveElement();
	}
	
}
