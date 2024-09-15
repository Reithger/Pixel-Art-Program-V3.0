package control.code;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Scanner;

import filemeta.config.Config;

public class CodeReference {

	/*
	 * Draw Mode
	 * Move Canvas Mode
	 * Color picker
	 * Fill tool
	 * 
	 * 
	 * Select Region (multiple contextual actions after this)
	 * Superlayer marking
	 * Pattern drawing
	 * Shape drawing (arbitrary polygon)
	 * 
	 */

//---  Constants   ----------------------------------------------------------------------------
	
	private final static String SEPARATOR = ";,;";
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private static HashMap<Integer, CodeInfo> codeInfo;
	
//---  Operations   ---------------------------------------------------------------------------
	
	public static void setup() {
		codeInfo = new HashMap<Integer, CodeInfo>();
		BufferedReader br = Config.retrieveFileReader("src/assets/setup.txt");
		try {
			Scanner sc = new Scanner(br);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] pieces = line.split(SEPARATOR);
				if(pieces.length != 3) {
					continue;
				}
				int ref = Integer.parseInt(pieces[0]);
				String path = pieces[1];
				if(path.equals("null")) {
					path = null;
				}
				String label = pieces[2];
				if(codeInfo.containsKey(ref)) {
					sc.close();
					throw new Exception("Double usage of same code-value in CodeReference");
				}
				codeInfo.put(ref, new CodeInfo(ref, path, label));
			}
			sc.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public static CodeInfo getCodeInfo(int code) {
		CodeInfo out = codeInfo.get(code);
		if(out == null) {
			System.out.println("Attempted to access CodeInfo object that had not been setup for code value: " + code);
			return new CodeInfo(code, null, "?");
		}
		return out;
	}
	
	public static String getCodeImagePath(int code) {
		CodeInfo use = getCodeInfo(code);
		if(use != null) {
			return use.getImagePath();
		}
		return "./assets/placeholder.png";
	}
	
	public static String getCodeLabel(int code) {
		CodeInfo use = getCodeInfo(code);
		if(use != null) {
			return use.getLabel();
		}
		return "";
	}
	
	public static HashMap<Integer, String> getCodeTooltips(){
		HashMap<Integer, String> out = new HashMap<Integer, String>();
		for(int i : codeInfo.keySet()) {
			out.put(i, codeInfo.get(i).getLabel());
		}
		return out;
	}
	
//---  TileGrid Mappings   --------------------------------------------------------------------

	public final static String[] REF_DRAW_TYPE_PATHS = new String[] {CodeReference.IMAGE_PATH_SQUARE_ICON,
			CodeReference.IMAGE_PATH_CIRCLE_ICON, CodeReference.IMAGE_PATH_TAPER_ICON};
	
		private final static String IMAGE_PATH_CIRCLE_ICON = "./assets/circle_icon.png";
		private final static String IMAGE_PATH_SQUARE_ICON = "./assets/square_icon.png";
		private final static String IMAGE_PATH_TAPER_ICON = "./assets/circle_icon.png";
		
	public final static int[] REF_COLOR_OPTION_CODES = new int[] {CodeReference.CODE_PEN_PALLETTE_PREV, CodeReference.CODE_PEN_PALLETTE_REMOVE,
																  CodeReference.CODE_PEN_PALLETTE_NEXT, CodeReference.CODE_PEN_PALLETTE_NEW,
																  CodeReference.CODE_COLOR_ADD, CodeReference.CODE_COLOR_EDIT,
																  CodeReference.CODE_COLOR_REMOVE, CodeReference.CODE_PEN_TOGGLE_BLEND};

		public final static int CODE_COLOR_ADD =72;
		public final static int CODE_COLOR_REMOVE =73;
		public final static int CODE_COLOR_EDIT =74;
		public final static int CODE_PEN_TOGGLE_BLEND =97;
	
	public final static int[] REF_PEN_MODE_CODES = new int[] {CodeReference.CODE_PEN_MODE_DRAW, CodeReference.CODE_PEN_MODE_MOVE_CANVAS,
			CodeReference.CODE_PEN_MODE_COLOR_PICK, CodeReference.CODE_PEN_MODE_FILL, CodeReference.CODE_PEN_REGION_MODE_FILL,
			CodeReference.CODE_PEN_REGION_MODE_OUTLINE, CodeReference.CODE_PEN_REGION_MODE_COPY, CodeReference.CODE_PEN_REGION_MODE_PASTE};

		public final static int CODE_PEN_MODE_MOVE_CANVAS =67;
		public final static int CODE_PEN_MODE_FILL =99;
		public final static int CODE_PEN_MODE_COLOR_PICK =100;
		public final static int CODE_PEN_MODE_DRAW = 101;
	
		public final static int CODE_PEN_REGION_MODE_FILL = 102;
		public final static int CODE_PEN_REGION_MODE_OUTLINE = 103;
		public final static int CODE_PEN_REGION_MODE_COPY = 104;
		public final static int CODE_PEN_REGION_MODE_PASTE = 105;
		
//---  Name References   ----------------------------------------------------------------------
	
	public final static String REF_SELECTION_MODE = "selection_mode";
	public final static String REF_COLOR_GRID = "color_grid";
	public final static String REF_COLOR_OPTIONS = "color_options";
	public final static String REF_PEN_TYPE_GRID = "pen_type_grid";
	public final static String REF_PEN_SIZE = "pen_det";
	public final static String REF_PEN_BLEND = "pen_blen";
	
//---  File Stuff   ---------------------------------------------------------------------------

	//-- Integrated  ------------------------------------------
	
	public final static int CODE_OPEN_FILE =50;
	public final static int CODE_SAVE_THING =51;
	public final static int CODE_SAVE_AS =52;
	public final static int CODE_DUPLICATE_THING =53;
	public final static int CODE_RENAME =54;
	public final static int CODE_EXIT =55;
	public final static int CODE_CLOSE_THING =58;
	
	//-- Partially Integrated  --------------------------------

	public final static int CODE_NEW_THING =56;
	
	//-- Not Integrated  --------------------------------------
	
	public final static int CODE_OPEN_META =57;

//---  SettingsBar Stuff   --------------------------------------------------------------------
	
	//-- Integrated  ------------------------------------------
	
	public final static int CODE_TOGGLE_TOOLTIPS = 106;
	public final static int CODE_MAXIMIZE_CANVAS = 107;
	
	//-- Partially Integrated  --------------------------------

	//-- Not Integrated  --------------------------------------
	
//---  Corkboard Page Stuff   -----------------------------------------------------------------

	//-- Integrated  ------------------------------------------
	
	public final static int CODE_INCREASE_ZOOM =59;
	public final static int CODE_DECREASE_ZOOM =60;
	public final static int CODE_RESIZE =61;
	public final static int CODE_HEADER =62;
	public final static int CODE_ADD_LAYER =64;
	public final static int CODE_DISPLAY_PIXEL_OUTLINE=114;

	public final static int CODE_UNDO_CHANGE =84;
	public final static int CODE_REDO_CHANGE =85;
	public final static int CODE_LAYER_DISPLAY_ALL =86;
	public final static int CODE_LAYER_DISPLAY_BENEATH =87;
	public final static int CODE_LAYER_DISPLAY_ACTIVE =88;
	public final static int CODE_ACTIVE_LAYER_UP =89;
	public final static int CODE_ACTIVE_LAYER_DOWN =90;
	public final static int CODE_MOVE_LAYER_UP =95;
	public final static int CODE_MOVE_LAYER_DOWN =96;
	
	public final static int CODE_NEW_DRAWING_PAGE = 500;
	
	//-- Partially Integrated  --------------------------------

	public final static int CODE_REMOVE_LAYER =65;
	public final static int CODE_MOVE_LAYER =66;
	public final static int CODE_EDIT_KEYBINDINGS = 112;
	
	//-- Not Integrated  --------------------------------------
	
	public final static int CODE_RANGE_LAYER_SELECT =1500;
	
	public final static int CODE_RESIZE_CANVAS =75;
	public final static int CODE_INCREMENT_CANVAS_WID =76;
	public final static int CODE_DECREMENT_CANVAS_WID =77;
	public final static int CODE_INCREMENT_CANVAS_HEI =78;
	public final static int CODE_DECREMENT_CANVAS_HEI =79;
	
	public final static int CODE_TOGGLE_CORKBOARD_BUTTONS = 113;
	
//---  Drawing Stuff   ------------------------------------------------------------------------
	
	//-- Corkboard Convenience Buttons  -----------------------
	
	
	public final static int[] REF_CORKBOARD_DEFAULT_BUTTONS = new int[] {
			CODE_PEN_MODE_MOVE_CANVAS,
			CODE_INCREASE_ZOOM,
			CODE_DECREASE_ZOOM,
			CODE_UNDO_CHANGE,
			CODE_REDO_CHANGE,
			CODE_LAYER_DISPLAY_ALL,
			CODE_LAYER_DISPLAY_BENEATH,
			CODE_LAYER_DISPLAY_ACTIVE,
			CODE_ADD_LAYER,
			CODE_ACTIVE_LAYER_UP,
			CODE_ACTIVE_LAYER_DOWN,
			CODE_MOVE_LAYER_UP,
			CODE_MOVE_LAYER_DOWN,
		};
	
	public final static int[] REF_CORKBOARD_HEADER_BUTTONS = new int[] {
			CODE_CLOSE_THING,
			CODE_TOGGLE_CORKBOARD_BUTTONS,
			CODE_MAXIMIZE_CANVAS,
			CODE_DISPLAY_PIXEL_OUTLINE
	};
	
	
	//-- Integrated  ------------------------------------------
	
	public final static int CODE_RANGE_SELECT_COLOR =2000;
	public final static int CODE_RANGE_SELECT_DRAW_TYPE =2500;
	
	public final static int CODE_PEN_SIZE_INCREMENT =68;
	public final static int CODE_PEN_SIZE_DECREMENT =69;
	public final static int CODE_UPDATE_COLOR =80;
	public final static int CODE_UPDATE_PEN_SIZE =81;
	public final static int CODE_UPDATE_PEN_BLEND =82;
	public final static int CODE_UPDATE_PEN_TYPE =83;
	public final static int CODE_UPDATE_COLOR_OPTIONS =91;
	public final static int CODE_PEN_INCREMENT_BLEND_QUOTIENT =92;
	public final static int CODE_PEN_DECREMENT_BLEND_QUOTIENT =93;
	public final static int CODE_UPDATE_SELECTION_MODE =94;
	public final static int CODE_PERFORM_REFRESH =98;

	//-- Partially Integrated  --------------------------------

	public final static int CODE_PEN_SIZE_SET =70;
	public final static int CODE_PEN_SET_BLEND_QUOTIENT =71;
	
	//-- Not Integrated  --------------------------------------
		//TODO: Save color set palletes for re-access without bloating main color selection

	public final static int CODE_PEN_PALLETTE_NEXT = 108;
	public final static int CODE_PEN_PALLETTE_PREV = 109;
	public final static int CODE_PEN_PALLETTE_NEW = 110;
	public final static int CODE_PEN_PALLETTE_REMOVE = 111;
	
	
}
