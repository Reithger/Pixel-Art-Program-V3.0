package manager.pen.drawing.drawtype;

/**
 * 
 * The 'Taper' draw type is basically the Circle draw but with shrinking size as
 * the current drawing instance goes on for longer until it draws at size 1 for
 * the remainder.
 * 
 * Should look into variations of this and options to set that adjust how quickly/slowly
 * the taper occurs.
 * 
 * @author Reithger
 *
 */

public class DrawTypeTapering implements DrawType{
	
	private int decline;
	
	@Override
	public Integer[][] draw(Integer col, int penSize) {
		int size = (int) (penSize - (penSize * (decline / 100.0)));
		size = size > 0 ? size : 1;
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
		decline = in == -1 ? decline : in;
	}
}
