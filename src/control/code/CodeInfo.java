package control.code;

public class CodeInfo {

	private int code;
	private String imagePath;
	private String label;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public CodeInfo(int inCode, String path, String inLabel) {
		code = inCode;
		imagePath = path;
		label = inLabel;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getCode() {
		return code;
	}
	
	public String getImagePath() {
		return imagePath == null ? "./assets/placeholder.png" : imagePath;
	}
	
	public String getLabel() {
		return label;
	}
	
}
