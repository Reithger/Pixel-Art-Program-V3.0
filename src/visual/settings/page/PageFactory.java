package visual.settings.page;

import java.util.ArrayList;

public class PageFactory {

	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * This function generates an ArrayList of Page subclasses that specialize into
	 * each type of menu for the user to interact with (File options, Drawing options, and
	 * a Settings page eventually).
	 * 
	 * @return - ArrayList of Page objects
	 */
	
	public static ArrayList<Page> generateStartingPages(){
		ArrayList<Page> out = new ArrayList<Page>();
		out.add(generateFilePage());
		out.add(generateDrawingPage());
		//out.add(generateSettingsPage());
		return out;
	}
	
	private static Page generateFilePage() {
		return new FilePage();
	}
	
	private static Page generateDrawingPage() {
		return new DrawToolsPage();
	}
	
	private static Page generateSettingsPage() {
		return new SettingsPage();
	}
	
	
}
