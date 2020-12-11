package visual.drawboard.corkboard.buttons;

public class ButtonInformation{

//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String imagePath;
	private int code;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ButtonInformation(String inNom, String inPath, int inCode) {
		name = inNom;
		imagePath = inPath;
		code = inCode;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public int getCode() {
		return code;
	}

}
