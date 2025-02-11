package control.code;

public class CodeInfo {
	
	public final static String DEFAULT_IMAGE = "./assets/placeholder.png";

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
		return imagePath == null ? DEFAULT_IMAGE : imagePath;
	}
	
	public String getLabel() {
		return label;
	}
	
}
