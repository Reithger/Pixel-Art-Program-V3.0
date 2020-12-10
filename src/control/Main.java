package control;

import manager.pen.drawing.LineCalculator;
import manager.pen.drawing.Point;

public class Main {

	public static void main(String[] args) {
		
		/*for(Point p : LineCalculator.getPointsBetwixt(new Point(404, 418), new Point(405, 418))) {
			System.out.println(p);
		}*/
		
		new PixelArtDrawer();
	}
	
}