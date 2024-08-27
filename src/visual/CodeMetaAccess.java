package visual;

import java.util.ArrayList;

public interface CodeMetaAccess {

	public abstract String getCodeImagePath(int code);
	
	public abstract String getCodeLabel(int code);
	
	public abstract ArrayList<String> getCodeImagePaths(int[] codes);
	
	public abstract ArrayList<String> getCodeLabels(int[] codes);
	
}
