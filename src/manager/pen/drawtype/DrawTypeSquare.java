package manager.pen.drawtype;

import java.awt.Color;


public class DrawTypeSquare implements DrawType{

	@Override
	public Color[][] draw(Color col, int penSize) {
		Color[][] out = new Color[penSize][penSize];
		for(int i = 0; i < penSize; i++) {
			for(int j = 0; j < penSize; j++) {
				out[i][j] = col;
			}
		}
		return out;
	}

}
