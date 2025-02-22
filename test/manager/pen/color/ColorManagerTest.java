package manager.pen.color;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorManagerTest {

	private ColorManager colorMan;
	
	@BeforeEach
	void setUp() {
		colorMan = new ColorManager();
	}
	
	@Test
	void testEditColor() {
		Color editColor = new Color(150, 125, 80);
		colorMan.editColor(0, editColor.getRGB());
		Color test = new Color(colorMan.getColor(0));
		
		colorCompare(150, 125, 80, test);
		
		colorMan.editColor(0, 15, 15, 15, 0);
		test = new Color(colorMan.getColor(0));
		colorCompare(165, 140, 95, test);
		
		colorMan.editColor(0, -500, -500, -500, 0);
		test = new Color(colorMan.getColor(0));
		colorCompare(0, 0, 0, test);
		
	}

	@Test
	void testAddColor() {
		assertEquals(10, colorMan.getCurrentPalletSize());
		Color newCol = new Color(120, 150, 180);
		colorMan.addColor(newCol.getRGB());
		colorCompare(newCol, new Color(colorMan.getColor(300)));
		assertEquals(11, colorMan.getCurrentPalletSize());
		newCol = new Color(33, 22, 11);
		colorMan.addColor(newCol.getRGB());
		colorCompare(newCol, new Color(colorMan.getColor(300)));
		assertEquals(12, colorMan.getCurrentPalletSize());
	}
	
	@Test
	void testRemoveColor() {
		assertEquals(10, colorMan.getCurrentPalletSize());
		Color store = new Color(colorMan.getColor(0));
		colorMan.removeColor(0);
		assertEquals(9, colorMan.getCurrentPalletSize());
		colorNotCompare(store, new Color(colorMan.getColor(0)));
		store = new Color(colorMan.getColor(5));
		colorMan.removeColor(4);
		colorCompare(store, new Color(colorMan.getColor(4)));
		for(int i = 0; i < 8; i++) {
			colorMan.removeColor(0);
		}
		colorCompare(Color.black, new Color(colorMan.getColor(0)));		
	}
	
	@Test
	void testAddPallet() {
		assertEquals(10, colorMan.getCurrentPalletSize());
		for(int i = 0; i < 5; i++) {
			colorMan.removeColor(5);
		}
		assertEquals(5, colorMan.getCurrentPalletSize());
		assertEquals(1, colorMan.getNumPallettes());
		colorMan.addPallet();
		colorMan.setCurrPallet(1);
		assertEquals(2, colorMan.getNumPallettes());
		assertEquals(10, colorMan.getCurrentPalletSize());
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.black.getRGB());
		cols.add(Color.blue.getRGB());
		cols.add(Color.white.getRGB());
		colorMan.addPallet(cols);
		colorMan.setCurrPallet(2);
		assertEquals(3, colorMan.getNumPallettes());
		assertEquals(3, colorMan.getCurrentPalletSize());
		colorCompare(Color.blue, new Color(colorMan.getColor(1)));
	}
	
	@Test
	void testRemovePallet() {
		assertEquals(1, colorMan.getNumPallettes());
		assertEquals(0, colorMan.getCurrentPalletIndex());
		colorMan.addPallet();
		colorMan.addPallet();
		assertEquals(0, colorMan.getCurrentPalletIndex());
		assertEquals(3, colorMan.getNumPallettes());
		colorMan.removePallet(0);
		assertEquals(0, colorMan.getCurrentPalletIndex());
		colorMan.setCurrPallet(1);
		assertEquals(2, colorMan.getNumPallettes());
		colorMan.removePallet(1);
		assertEquals(0, colorMan.getCurrentPalletIndex());
		assertEquals(1, colorMan.getNumPallettes());
	}
	
	@Test
	void testSetCurrPallet() {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.blue.getRGB());
		cols.add(Color.green.getRGB());
		colorMan.addPallet(cols);
		Color one = new Color(colorMan.getColor(0));
		colorMan.setCurrPallet(1);
		Color two = new Color(colorMan.getColor(0));
		colorCompare(Color.black, one);
		colorCompare(Color.blue, two);
		colorNotCompare(one, two);
	}
	
	@Test
	void testSetActiveColor() {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.blue.getRGB());
		cols.add(Color.green.getRGB());
		colorMan.addPallet(cols);
		colorMan.setCurrPallet(1);
		colorMan.setActiveColor(1);
		colorCompare(Color.green, new Color(colorMan.getActiveColor()));
		colorMan.setActiveColor(0);
		colorCompare(Color.blue, new Color(colorMan.getActiveColor()));
	}
	
	@Test
	void testGetColors() {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.blue.getRGB());
		cols.add(Color.green.getRGB());
		colorMan.addPallet(cols);
		colorMan.setCurrPallet(1);
		ArrayList<Integer> pull = colorMan.getColors();
		for(int i = 0; i < cols.size(); i++) {
			assertEquals(cols.get(i), pull.get(i));
		}
	}
	
	@Test
	void testGetPallete() {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.blue.getRGB());
		cols.add(Color.green.getRGB());
		colorMan.addPallet(cols);
		colorMan.setCurrPallet(1);
		ArrayList<Integer> pull = colorMan.getPallette(1);
		for(int i = 0; i < cols.size(); i++) {
			assertEquals(cols.get(i), pull.get(i));
		}
		pull = colorMan.getPallette(0);
		assertEquals(10, pull.size());
		assertEquals(2, colorMan.getNumPallettes());
	}
	
	@Test
	void testGetActiveColor() {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		cols.add(Color.blue.getRGB());
		cols.add(Color.green.getRGB());
		cols.add(Color.red.getRGB());
		cols.add(Color.yellow.getRGB());
		colorMan.addPallet(cols);
		colorMan.setCurrPallet(1);
		colorMan.setActiveColor(1);
		colorCompare(Color.green, new Color(colorMan.getActiveColor()));
		colorMan.setActiveColor(3);
		colorCompare(Color.yellow, new Color(colorMan.getActiveColor()));
		colorMan.setActiveColor(55);
		colorCompare(Color.yellow, new Color(colorMan.getActiveColor()));
	}
	
	private void colorCompare(int red, int green, int blue, Color col) {
		assertEquals(red, col.getRed());
		assertEquals(green, col.getGreen());
		assertEquals(blue, col.getBlue());
	}
	
	private void colorCompare(Color col1, Color col2) {
		assertEquals(col1.getRed(), col2.getRed());
		assertEquals(col1.getGreen(), col2.getGreen());
		assertEquals(col1.getBlue(), col2.getBlue());
	}
	
	private void colorNotCompare(Color col1, Color col2) {
		boolean anyDiff = false;
		if(col1.getRed() != col2.getRed()) {
			anyDiff = true;
		}
		if(col1.getGreen() != col2.getGreen()) {
			anyDiff = true;
		}
		if(col1.getBlue() != col2.getBlue()) {
			anyDiff = true;
		}
		assertTrue(anyDiff);
	}
	
}
