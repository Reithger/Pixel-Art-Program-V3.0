package manager.pen.drawing.drawtype;

public class DrawTypeSquare implements DrawType{

//---  Operations   ---------------------------------------------------------------------------

	@Override
	public Integer[][] draw(Integer col, int penSize) {
		Integer[][] out = new Integer[penSize][penSize];
		for(int i = 0; i < penSize; i++) {
			for(int j = 0; j < penSize; j++) {
				out[i][j] = col;
			}
		}
		return out;
	}
	
	@Override
	public void tellAge(int in) {
		
	}
}
