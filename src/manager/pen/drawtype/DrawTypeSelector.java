package manager.pen.drawtype;

public class DrawTypeSelector {

//---  Constants   ----------------------------------------------------------------------------
	
	public final static int PEN_DRAW_SQUARE = 0;
	public final static int PEN_DRAW_CIRCLE = 1;

	//TODO: Let user make dynamic draw patterns
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public static DrawType getDrawType(int index) {
		DrawType out = null;
		switch(index) {
			case PEN_DRAW_SQUARE:
				out = new DrawTypeSquare();
				break;
			case PEN_DRAW_CIRCLE:
				out = new DrawTypeCircle();
				break;
			default:
				out = new DrawTypeSquare();
				break;
		}
		return out;
	}
	
	public static int[] getDrawTypes() {
		return new int[] {PEN_DRAW_SQUARE, PEN_DRAW_CIRCLE};
	}
	
}
