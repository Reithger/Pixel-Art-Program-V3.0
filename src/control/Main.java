package control;

/**
 * 
 * ---  UI   ----------------------------------------------------------------------------------
 * 
 * TODO: Allow options menu to be moved to sides of screen/minimized instead of at top
 * TODO: Adjust size of menu screen for visibility
 * TODO: Pallette selector for Colors (as in, save a whole pallette of colors to load back in)
 * TODO: Constant feedback of what 'pen mode' you're currently in
 * TODO: Customize sidebar buttons on the canvas (position/presence)
 * TODO: Feedback that saving has worked
 * TODO: Resizing of canvas
 * TODO: Smoother resizing of the Corkboard window for the canvases
 * TODO: Feedback on layer information (currently on which, display all)
 * TODO: Option to remove layers
 * TODO: View all layers at once (popup menu for shuffling them or removing them that way)
 * TODO: Change the amount of space given to a Tile in the Drawing menu
 * TODO: Canvas draws outside of its Corkboard
 * TODO: Allow user to change background color, or jusy have the entire background be a default canvas that reloads
 * TODO: Moving Corkboard while able to move canvas causes issues
 * 
 * ---  Backend   -----------------------------------------------------------------------------
 * 
 * TODO: Make the fill algorithm more efficient
 * TODO: Animations. Oof.
 * TODO: Better tuned Blend Quotient values (most above ~20 just auto draw the color, basically)
 * TODO: Produce gif of progress for an image using the saved changes
 * TODO: More types of brushes (one that has a starting size and gradually shrinks; exponential/logarithmic not linear decay)
 * TODO: Incorporate pen sensitivity into how brushes draw
 * TODO: Custom pen shapes; user gets a little canvas to draw their shape on, allow to save/load them
 * TODO: Blend local region of colors
 * 
 * ---  Control   -----------------------------------------------------------------------------
 * 
 * TODO: Pallette saving/automatic loading for reuse via Pallette color selector
 * TODO: In-place color changing via rgb value manipulation
 * TODO: Right click options for more convenient means of changing properties of a thing
 * TODO: More default keybindings and the ability to edit them
 * TODO: Canvas sidebar buttons don't have informative overlay text
 * TODO: Ability to move colors around on their pallette 
 * TODO: Drop down menu on Menu items for more options that aren't nice to generally show; click/drag tiles to move them into more direct position
 * TODO: Settings menu for adjusting meta settings (font size/size of buttons/other stuff)
 * TODO: Saving a config file with info for stuff
 * TODO: Set default save location globally
 * 
 * ---  Stuff I Gotta Do   --------------------------------------------------------------------
 * 
 * TODO: Preset pallettes available (halloween/christmas colors and whatnot)
 * TODO: Make more icons for the buttons
 * TODO: Customize file browser in SVI, OS default is very meh
 * TODO: Make it prettier
 * 
 * 
 * @author Ada Clevinger
 *
 */

public class Main {

//---  Operations   ---------------------------------------------------------------------------
	
	public static void main(String[] args) {
		new PixelArtDrawer();
	}
	
}