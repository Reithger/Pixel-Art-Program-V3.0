package visual.settings.page;

public class DrawingPage extends Page {

	private final static String PAGE_NAME = "Drawing";
	
	public DrawingPage() {
		super(PAGE_NAME);
		addTileGrid(new String[] {"/assets/placeholder.png", "/assets/placeholder.png","/assets/placeholder.png","/assets/placeholder.png",}, "lab", new int[] {0, 0, 0, 0});
	}

}
