package manager.pen.color;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PalletTest {

	private Pallet pallet;
	
	@BeforeEach
	void setUp() {
		pallet = new Pallet();
	}
	
	@Test
	void testConstructArrayList() {
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(Color.red.getRGB());
		colors.add(Color.blue.getRGB());
		pallet = new Pallet(colors);
		assertEquals(0, pallet.getActiveColorIndex());
		assertEquals(Color.blue.getRGB(), pallet.getColor(1));
		assertEquals(2, pallet.getNumberColors());
		colors.clear();
		pallet = new Pallet(colors);
		assertEquals(10, pallet.getNumberColors());
	}
	
	@Test
	void testAddColor() {
		assertEquals(10, pallet.getNumberColors());
		Integer add = Color.cyan.getRGB();
		pallet.addColor(add);
		assertEquals(11, pallet.getNumberColors());
		assertEquals(add, pallet.getColor(10));
		for(int i = 0; i < 25; i++) {
			pallet.addColor(add);
		}
		assertEquals(pallet.getMaximumPalletSize(), pallet.getNumberColors());
	}
	
	@Test
	void testRemoveColor() {
		assertEquals(10, pallet.getNumberColors());
		Integer col = pallet.getColor(0);
		Integer col2 = pallet.getColor(1);
		pallet.removeColor(0);
		assertEquals(9, pallet.getNumberColors());
		assertNotEquals(col, pallet.getColor(0));
		assertEquals(col2, pallet.getColor(0));
		pallet.setActiveColor(10);
		int act = pallet.getActiveColorIndex();
		pallet.removeColor(8);
		assertEquals(8, pallet.getNumberColors());
		assertEquals(pallet.getActiveColorIndex(), act - 1);
	}
	
	@Test
	void testSetColor() {
		assertEquals(10, pallet.getNumberColors());
		pallet.setColor(4, Color.cyan.getRGB());
		assertEquals(10, pallet.getNumberColors());
		assertEquals(Color.cyan.getRGB(), pallet.getColor(4));
		pallet.setColor(15, Color.gray.getRGB());
		assertEquals(Color.gray.getRGB(), pallet.getColor(9));
	}
	
	@Test
	void testSetActiveColor() {
		pallet.setActiveColor(5);
		assertEquals(5, pallet.getActiveColorIndex());
		assertEquals(Pallet.DEFAULT_COLORS[5].getRGB(), pallet.getActiveColor());
		pallet.setActiveColor(10);
		assertEquals(9, pallet.getActiveColorIndex());
		assertEquals(Pallet.DEFAULT_COLORS[9].getRGB(), pallet.getActiveColor());
		pallet.setActiveColor(-5);
		assertEquals(0, pallet.getActiveColorIndex());
	}
	
	@Test
	void testGetMaximumPalletSize() {
		assertEquals(Pallet.MAXIMUM_SIZE, pallet.getMaximumPalletSize());
	}
	
	@Test
	void testGetColor() {
		assertEquals(Pallet.DEFAULT_COLORS[0].getRGB(), pallet.getColor(0));
		assertEquals(Pallet.DEFAULT_COLORS[3].getRGB(), pallet.getColor(3));
		assertEquals(Pallet.DEFAULT_COLORS[9].getRGB(), pallet.getColor(9));
	}
	
	@Test
	void testGetColors() {
		ArrayList<Integer> colors = pallet.getColors();
		assertEquals(pallet.getNumberColors(), colors.size());
		assertEquals(Pallet.DEFAULT_COLORS[0].getRGB(), colors.get(0));
	}

	
}
