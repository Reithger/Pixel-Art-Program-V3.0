package visual.settings.page;

import java.util.ArrayList;

public class PageFactory {

	
//---  Operations   ---------------------------------------------------------------------------
	
	public static ArrayList<Page> generateStartingPages(){
		ArrayList<Page> out = new ArrayList<Page>();
		out.add(generateFilePage());
		out.add(generateDrawingPage());
		out.add(generateSettingsPage());
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
