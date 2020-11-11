package visual.settings.page;

import visual.settings.SettingsBar;

public class PageFactory {

	private static boolean assignedRef;
	
	public static void assignReference(SettingsBar ref) {
		Page.assignReference(ref);
		assignedRef = true;
	}
	
	public static Page generateFilePage() {
		if(assignedRef)
			return new FilePage();
		else {
			error();
			return null;
		}
	}
	
	public static Page generateDrawingPage() {
		if(assignedRef)
			return new DrawingPage();
		else {
			error();
			return null;
		}
	}
	
	public static Page generateSettingsPage() {
		if(assignedRef)
			return new SettingsPage();
		else {
			error();
			return null;
		}
	}
	
	private static void error() {
		System.out.println("Static reference to a SettingsBar has not been performed for the Page abstract class.");
	}
	
}
