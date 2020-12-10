package manager.pen.drawtype;

import java.awt.Color;

public interface DrawType {

//---  Operations   ---------------------------------------------------------------------------

	public abstract Color[][] draw(Color col, int penSize);
	
}
