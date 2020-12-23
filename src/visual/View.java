package visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import control.InputHandler;
import filemeta.FileChooser;
import misc.Canvas;
import visual.composite.popout.PopoutAlert;
import visual.composite.popout.PopoutSelectList;
import visual.drawboard.DrawingBoard;
import visual.frame.WindowFrame;
import visual.popouts.PopoutColorDesigner;
import visual.popouts.PopoutConfirm;
import visual.popouts.PopoutInputRequest;
import visual.settings.SettingsBar;

public class View implements InputHandler{
	
//---  Constants   ----------------------------------------------------------------------------
	
	private final static int SCREEN_WIDTH = 1200;
	
	private final static int SCREEN_HEIGHT = 800;
	
	private final static double SETTINGS_VERT_RATIO = 1.0 / 7;

	private final static Color COLOR_BACKGROUND = new Color(188, 188, 188);
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private InputHandler reference;
	
	private WindowFrame frame;
	
	private SettingsBar options;
	
	private DrawingBoard body;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public View(InputHandler in) {
		//Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		//TODO: Make this dynamic to your screen size
		reference = in;
		frame = new WindowFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.getFrame().getContentPane().setBackground(COLOR_BACKGROUND);
		frame.setName("Test");
		options = new SettingsBar(0, 0, SCREEN_WIDTH, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), frame, this);
		body = new DrawingBoard(0, (int)(SCREEN_HEIGHT * SETTINGS_VERT_RATIO), SCREEN_WIDTH, (int)(SCREEN_HEIGHT * (1 - SETTINGS_VERT_RATIO)), frame, this);
	}

//---  Operations   ---------------------------------------------------------------------------
	
	//-- Input Handling  --------------------------------------
	
	public void handleCodeInput(int code, String context) {
		reference.handleCodeInput(code, context);
	}
	
	public void handleDrawInput(int x, int y, int duration, String nom) {
		reference.handleDrawInput(x, y, duration, nom);
	}
	
	public void handleKeyInput(char code, int keyType) {
		reference.handleKeyInput(code, keyType);
	}

	public String getTileContents(String ref) {
		return options.getTileContents(ref);
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
	
	public Color requestColorChoice(Color def) {
		PopoutColorDesigner pCD = new PopoutColorDesigner(300, 300, def);
		Color out = pCD.getChoice();
		pCD.dispose();
		return out;
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
	
	//-- Settings Bar Management  -----------------------------
	
	public void refreshActivePage() {
		options.refreshActivePage();
	}
	
	public void updateTileGridColors(String ref, ArrayList<Color> cols, int[] codes, int active) {
		options.updateTileGridColors(ref, cols, codes, active);
	}
	
	public void updateTileNumericSelector(String ref, int min, int max, int size) {
		options.updateNumericSelector(ref, min, max, size);
	}
	
	public void updateTileGridImages(String ref, ArrayList<String> paths, int[] codes, int index) {
		options.updateTileGridImages(ref, paths, codes, index);
	}
	
	public void toggleTooltips() {
		options.toggleTooltips();
	}
	
	//-- Drawing Board Management  ----------------------------
	
	public void rename(HashMap<String, String> mappings) {
		body.rename(mappings);
	}
	
	public void updateDisplay(String nom, Canvas[] imgs, int zoom) {
		body.updateDisplay(nom, imgs, zoom);
	}
	
	public void removeFromDisplay(String nom) {
		body.removeFromDisplay(nom);
	}
	
	public void addPicture(String nom, Canvas img) {
		body.generatePictureDisplay(nom, img);
	}
	
	public void addAnimation(String nom, Canvas[] img) {
		body.generateAnimationDisplay(nom, img);
	}

	public void duplicateThing(String old, String nom) {
		body.duplicateThing(old, nom);
	}
	
	public void setContentLock(String nom, boolean set) {
		body.setContentLock(nom, set);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getActiveElement() {
		return body.getActiveElement();
	}
	
}
