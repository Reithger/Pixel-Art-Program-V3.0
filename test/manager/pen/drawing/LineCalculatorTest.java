package manager.pen.drawing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

class LineCalculatorTest {

	@Test
	void testHorizontalLine() {
		Point a = new Point(5, 5);
		Point b = new Point(5, 10);
		HashSet<Point> betwixt = LineCalculator.getPointsBetwixt(a, b);
		assertTrue(betwixt.contains(a));
		assertTrue(betwixt.contains(b));
		for(int i = 1; i < 5; i++) {
			Point c = new Point(5, 5 + i);
			assertTrue(betwixt.contains(c));
		}
		assertEquals(6, betwixt.size());
	}

	@Test
	void testVerticalLine() {
		Point a = new Point(5, 5);
		Point b = new Point(10, 5);
		HashSet<Point> betwixt = LineCalculator.getPointsBetwixt(a, b);
		assertTrue(betwixt.contains(a));
		assertTrue(betwixt.contains(b));
		for(int i = 1; i < 5; i++) {
			Point c = new Point(5 + i, 5);
			assertTrue(betwixt.contains(c));
		}
		assertEquals(6, betwixt.size());
	}
	
	@Test
	void testFortyFiveAngledLine() {
		Point a = new Point(5, 5);
		Point b = new Point(10, 10);
		HashSet<Point> betwixt = LineCalculator.getPointsBetwixt(a, b);
		assertTrue(betwixt.contains(a));
		assertTrue(betwixt.contains(b));
		for(int i = 1; i < 5; i++) {
			Point c = new Point(5 + i, 5 + i);
			assertTrue(betwixt.contains(c));
		}
		assertEquals(6, betwixt.size());
	}

}
