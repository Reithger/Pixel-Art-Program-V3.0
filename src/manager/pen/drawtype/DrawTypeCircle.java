package manager.pen.drawtype;

import java.awt.Color;

public class DrawTypeCircle implements DrawType{

	@Override
	public Color[][] draw(Color col, int penSize) {
		Color[][] out = new Color[penSize][penSize];
		double originX = penSize / 2.0;
		double originY = penSize / 2.0;
		for(int i = 0; i < penSize; i++) {
			for(int j = 0; j < penSize; j++) {
				if(Math.sqrt(Math.pow(originX - i, 2) + Math.pow(originY - j, 2)) <= penSize) {
					out[i][j] = col;
				}
				else {
					out[i][j] = null;
				}
			}
		}
		return out;
	}

}
