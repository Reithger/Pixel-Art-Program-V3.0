package control;

public class CodeReference {

//---  Draw Type Mappings   -------------------------------------------------------------------

	public final static int REF_MAX_DRAW_TYPES = 2;
	
//---  File Pathing   -------------------------------------------------------------------------
	
	public final static String[] REF_DRAW_TYPE_PATHS = new String[] {CodeReference.IMAGE_PATH_SQUARE_ICON, CodeReference.IMAGE_PATH_CIRCLE_ICON};
	private final static String IMAGE_PATH_CIRCLE_ICON = "./assets/circle_icon.png";
	private final static String IMAGE_PATH_SQUARE_ICON = "./assets/square_icon.png";
	
	public final static String IMAGE_PATH_ZOOM_IN = "./assets/grow_icon.png";
	public final static String IMAGE_PATH_ZOOM_OUT = "./assets/shrink_icon.png";
	public final static String IMAGE_PATH_RESIZE_CORKBOARD = "./assets/placeholder.png";
	
//---  Name References   ----------------------------------------------------------------------
	
	public final static String REF_COLOR_GRID = "color_grid";
	public final static String REF_PEN_TYPE_GRID = "pen_type_grid";
	public final static String REF_PEN_SIZE = "pen_det";
	public final static String REF_PEN_BLEND = "pen_blen";
	
//---  File Stuff   ---------------------------------------------------------------------------

	//-- Integrated  ------------------------------------------
	
	public final static int CODE_OPEN_FILE = 50;
	public final static int CODE_SAVE_THING = 51;
	public final static int CODE_SAVE_AS = 52;
	public final static int CODE_DUPLICATE_THING = 53;
	public final static int CODE_RENAME = 54;
	public final static int CODE_EXIT = 55;
	
	//-- Partially Integrated  --------------------------------

	public final static int CODE_NEW_THING = 56;
	public final static int CODE_CLOSE_THING = 58;
	
	//-- Not Integrated  --------------------------------------
	
	public final static int CODE_OPEN_META = 57;

//---  Corkboard Stuff   ----------------------------------------------------------------------

	//-- Integrated  ------------------------------------------
	
	public final static int CODE_INCREASE_ZOOM = 59;
	public final static int CODE_DECREASE_ZOOM = 60;
	public final static int CODE_RESIZE = 61;
	public final static int CODE_HEADER = 62;
	public final static int CODE_INTERACT_CONTENT = 63;
	
	//-- Partially Integrated  --------------------------------

	public final static int CODE_ADD_LAYER = 64;
	public final static int CODE_REMOVE_LAYER = 65;
	public final static int CODE_MOVE_LAYER = 66;
	
	//-- Not Integrated  --------------------------------------

	public final static int CODE_RANGE_LAYER_SELECT = 1500;
	public final static int CODE_LOCK_CANVAS = 67;
	public final static int CODE_RESIZE_CANVAS = 75;
	
	public final static int CODE_INCREMENT_CANVAS_WID = 76;
	public final static int CODE_DECREMENT_CANVAS_WID = 77;
	public final static int CODE_INCREMENT_CANVAS_HEI = 78;
	public final static int CODE_DECREMENT_CANVAS_HEI = 79;
	
	
//---  Drawing Stuff   ------------------------------------------------------------------------
	
	//-- Integrated  ------------------------------------------
	
	public final static int CODE_RANGE_SELECT_COLOR = 2000;
	public final static int CODE_PEN_SIZE_INCREMENT = 68;
	public final static int CODE_PEN_SIZE_DECREMENT = 69;
		
	//-- Partially Integrated  --------------------------------

	
	//-- Not Integrated  --------------------------------------
													//TODO: Save color set palletes for re-access without bloating main color selection
	public final static int CODE_RANGE_SELECT_DRAW_TYPE = 2500;
	
	public final static int CODE_PEN_SIZE_SET = 70;
	
	public final static int CODE_PEN_SET_BLEND_QUOTIENT = 71;
	
	public final static int CODE_COLOR_ADD = 72;
	public final static int CODE_COLOR_REMOVE = 73;
	public final static int CODE_COLOR_EDIT = 74;
	
	public final static int CODE_UPDATE_COLOR = 80;
	public final static int CODE_UPDATE_PEN_SIZE = 81;
	public final static int CODE_UPDATE_PEN_BLEND = 82;
	public final static int CODE_UPDATE_PEN_TYPE = 83;
	
}
