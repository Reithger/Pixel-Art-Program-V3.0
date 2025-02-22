package manager.pen.drawing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {

	@Test
	void testBasicGet() {
		Point a = new Point(5, 5);
		Point b = new Point(10, 5);
		assertEquals(5, a.getX());
		assertEquals(5, a.getY());
		assertEquals(10, b.getX());
		assertEquals(5, b.getY());
	}
	
	@Test
	void testHashCode() {
		Point a = new Point(10, 15);
		assertEquals("(10, 15)".hashCode(), a.hashCode());
		Point b = new Point(-15, 55);
		assertEquals("(-15, 55)".hashCode(), b.hashCode());
	}
	
	@Test
	void testCompareTo() {
		Point a  = new Point(5, 5);
		Point b = new Point(5, 5);
		assertEquals(0, a.compareTo(b));
		Point c = new Point(0, 10);
		assertEquals(-1, c.compareTo(a));
		assertEquals(1, a.compareTo(c));
	}
	
	@Test
	void testEquals() {
		Point a = new Point(5, 5);
		Point b = new Point(5, 10);
		Point c = new Point(5, 5);
		assertTrue(a.equals(c));
		assertFalse(a.equals(b));
		assertFalse(b.equals(c));
	}
	
	@Test
	void testToString() {
		Point a = new Point(10, 15);
		assertEquals("(10, 15)", a.toString());
		Point b = new Point(-15, 55);
		assertEquals("(-15, 55)", b.toString());
	}
	
	@Test
	void testCompare() {
		
	}


}
