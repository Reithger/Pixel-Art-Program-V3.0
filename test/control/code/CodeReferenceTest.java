package control.code;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CodeReferenceTest {

	@BeforeEach
	void setUp() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		assertEquals(3, CodeReference.getNumberCodes());
		assertEquals("/assets/add_layer_icon.png", CodeReference.getCodeImagePath(15));
	}
	
	@AfterEach
	void tearDown() {
		CodeReference.tearDown();
	}
	
	@Test
	void testSetupFilePath() {
		assertEquals(3, CodeReference.getNumberCodes());
	}
	
	@Test
	void testSetupArrayList() {
		CodeReference.tearDown();
		ArrayList<String> testData = new ArrayList<String>();
		int num = 15;
		String path = "/assets/circle_icon.png";
		String desc = "test example";
		String SEP = CodeReference.SEPARATOR;
		testData.add(num + SEP + path + SEP + desc);
		assertDoesNotThrow(() -> {CodeReference.setup(testData);});
		assertEquals(1, CodeReference.getNumberCodes());
		assertEquals(desc, CodeReference.getCodeLabel(num));
		assertEquals(path, CodeReference.getCodeImagePath(num));
		int num2 = 30;
		String path2 = "/assets/redo_icon.png";
		String desc2 = "another example";
		CodeReference.tearDown();
		testData.add(num2 + SEP + path2 + SEP + desc2);
		assertDoesNotThrow(() -> {CodeReference.setup(testData);});
		assertEquals(2, CodeReference.getNumberCodes());
		assertEquals(desc, CodeReference.getCodeLabel(num));
		assertEquals(path, CodeReference.getCodeImagePath(num));
		assertEquals(desc2, CodeReference.getCodeLabel(num2));
		assertEquals(path2, CodeReference.getCodeImagePath(num2));
		// Multiple tests that certain attempts to add don't work when they shouldn't
		testData.add("arb;,;arb;,;arb;,;arb");
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup(testData);});
		assertEquals(2, CodeReference.getNumberCodes());
		testData.add("15;,;null;,;rep");
		CodeReference.tearDown();
		assertThrows(Exception.class, () -> {CodeReference.setup(testData);});
		assertEquals(2, CodeReference.getNumberCodes());
	}
	
	@Test
	void testTearDown() {
		CodeReference.tearDown();
		assertEquals(0, CodeReference.getNumberCodes());
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		assertEquals(3, CodeReference.getNumberCodes());
		CodeReference.tearDown();
		assertEquals(0, CodeReference.getNumberCodes());
	}
	
	@Test
	void testGetCodeInfo() {
		CodeInfo ci;
		try{
			ci = CodeReference.getCodeInfo(15);
		}
		catch(Exception e) {
			ci = null;
			fail(e.getMessage());
		}
		assertNotEquals(null, ci);
		assertEquals(15, ci.getCode());
		assertThrows(Exception.class, () -> {CodeReference.getCodeInfo(88888);});
	}
	
	@Test
	void testGetMysteryCodeInfo() {
		CodeInfo ci = CodeReference.getMysteryCodeInfo(35);
		assertEquals(35, ci.getCode());
		assertEquals(CodeReference.DEFAULT_BACKUP_PATH, ci.getImagePath());
		assertEquals("?", ci.getLabel());
	}
	
	@Test
	void testGetCodeImagePath() {
		assertEquals("/assets/add_layer_icon.png", CodeReference.getCodeImagePath(15));
		assertEquals(CodeReference.DEFAULT_BACKUP_PATH, CodeReference.getCodeImagePath(25));
		assertEquals(CodeReference.DEFAULT_BACKUP_PATH, CodeReference.getCodeImagePath(55));
	}
	
	@Test
	void testGetCodeLabel() {
		assertEquals("example code reference value", CodeReference.getCodeLabel(15));
		assertEquals("?", CodeReference.getCodeLabel(55));
	}
	
	@Test
	void testGetCodeTooltips() {
		HashMap<Integer, String> tooltips = CodeReference.getCodeTooltips();
		assertEquals(tooltips.get(15), "example code reference value");
		assertEquals(tooltips.get(20), "another example");
		assertEquals(tooltips.get(25), "null example");
	}
	
	

}
