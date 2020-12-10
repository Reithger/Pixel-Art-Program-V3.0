package manager.pen.drawing;

import java.util.HashSet;

public class LineCalculator {

//---  Operations   ---------------------------------------------------------------------------
	
	public static HashSet<Point> getPointsBetwixt(Point a, Point b){
		HashSet<Point> out = new HashSet<Point>();
		out.add(a);
		out.add(b);
		
		int x1 = a.getX();
		int x2 = b.getX();
		int y1 = a.getY();
		int y2 = b.getY();
		
		boolean xBig = x1 < x2;
		boolean yBig = y1 < y2;
		
		if(x1 == x2) {
			for(int i = yBig ? y1 : y2; i < (yBig ? y2 : y1); i++) {
				out.add(new Point(x1, i));
			}
			return out;
		}
		
		if(y1 == y2) {
			for(int i = xBig ? x1 : x2; i < (xBig ? x2 : x1); i++) {
				out.add(new Point(i, y1));
			}
			return out;
		}
		
		double m = ((double)y2 - (double)y1) / ((double)x2 - (double)x1);
		double vB = (double)y1 - (m * (double)x1);
		
		
		for(int i = xBig ? x1 : x2; i < (xBig ? x2 : x1); i++) {
			double y = (m * (double)i + vB);
			Point n = new Point(i, (int)y);
			out.add(n);
		}
		
		for(int i = yBig ? y1 : y2; i < (yBig ? y2 : y1); i++) {
			double x = ((double)i - vB) / m;
			Point n = new Point((int)x, i);
			out.add(n);
		}
	
		return out;
	}
	
}
