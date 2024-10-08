package manager.pen.drawing;

import java.util.Comparator;

public class Point implements Comparable<Point>, Comparator<Point> {

//---  Instance Variables   -------------------------------------------------------------------
	
	private int x;
	private int y;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Point(int xIn, int yIn) {
		x = xIn;
		y = yIn;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return (this.toString()).hashCode();
	}

	@Override
	public int compareTo(Point o) {
		int out = compareInteger(getX(), o.getX());
		out = out == 0 ? compareInteger(getY(), o.getY()) : out;
		return out;
	}
	
	@Override
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y +")";
	}

	@Override
	public int compare(Point o1, Point o2) {
		return o1.toString().compareTo(o2.toString());
	}
	
	private int compareInteger(int a, int b) {
		if(a < b) {
			return -1;
		}
		else if(b < a) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
}
