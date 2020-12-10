package visual.settings.page;

import control.InputHandler;

public class PageFactory {

	private static boolean assignedRef;
	
	public static void assignReference(InputHandler ref) {
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
			return new DrawToolsPage();
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
