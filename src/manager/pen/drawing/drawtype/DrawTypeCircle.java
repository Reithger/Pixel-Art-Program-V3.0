package manager.pen.drawing.drawtype;

public class DrawTypeCircle implements DrawType{

//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public Integer[][] draw(Integer col, int penSize) {
		int size = penSize;
		if(penSize % 2 == 0) {
			size++;
		}
		Integer[][] out = new Integer[size][size];
		double originX = size / 2.0;
		double originY = size / 2.0;
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out[i].length; j++) {
				if(Math.sqrt(Math.pow(i + .5 - originX, 2) + Math.pow(j + .5 - originY, 2)) <= penSize / 2.0) {
					out[i][j] = col;
				}
				else {
					out[i][j] = null;
				}
			}
		}
		return out;
	}
	
	@Override
	public void tellAge(int in) {
		
	}

}
