package manager.pen.color;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

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
		Integer col4 = colorMan.getColor(4);
		Color color = new Color(col4);
		colorMan.editColor(4, 15, 15, 15, 0);
		Color color2 = new Color(colorMan.getColor(4));
		assertEquals((color.getRed() + 15) % 255, color2.getRed());
		assertEquals((color.getGreen() + 15) % 255, color2.getGreen());
		assertEquals((color.getBlue() + 15) % 255, color2.getBlue());
		colorMan.editColor(4, -500, -500, -500, 0);
		color2 = new Color(colorMan.getColor(4));
		assertEquals(0, color2.getRed());
		assertEquals(0, color2.getGreen());
		assertEquals(0, color2.getBlue());
		
	}
	
	@Test
	void testAddColor() {
		
	}
	
	@Test
	void testRemoveColor() {
		
	}
	
	@Test
	void testAddPallet() {
		
	}
	
	@Test
	void testRemovePallet() {
		
	}
	
	@Test
	void testSetCurrPallet() {
		
	}
	
	@Test
	void testSetActiveColor() {
		
	}
	
	@Test
	void testGetColors() {
		
	}
	
	@Test
	void testGetPallete() {
		
	}
	
	@Test
	void testGetMisc() {
		
	}

}
