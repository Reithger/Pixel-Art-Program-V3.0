package control.code;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class CodeReferenceTest {

	@Test
	void testSetupFilePath() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		assertEquals(CodeReference.getNumberCodes(), 3);
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
		assertEquals(CodeReference.getNumberCodes(), 1);
		assertEquals(CodeReference.getCodeLabel(num), desc);
		assertEquals(CodeReference.getCodeImagePath(num), path);
		int num2 = 30;
		String path2 = "/assets/redo_icon.png";
		String desc2 = "another example";
		CodeReference.tearDown();
		testData.add(num2 + SEP + path2 + SEP + desc2);
		assertDoesNotThrow(() -> {CodeReference.setup(testData);});
		assertEquals(CodeReference.getNumberCodes(), 2);
		assertEquals(CodeReference.getCodeLabel(num), desc);
		assertEquals(CodeReference.getCodeImagePath(num), path);
		assertEquals(CodeReference.getCodeLabel(num2), desc2);
		assertEquals(CodeReference.getCodeImagePath(num2), path2);
		// Multiple tests that certain attempts to add don't work when they shouldn't
		testData.add("arb;,;arb;,;arb;,;arb");
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup(testData);});
		assertEquals(CodeReference.getNumberCodes(), 2);
		testData.add("15;,;null;,;rep");
		CodeReference.tearDown();
		assertThrows(Exception.class, () -> {CodeReference.setup(testData);});
		assertEquals(CodeReference.getNumberCodes(), 2);
	}
	
	@Test
	void testTearDown() {
		CodeReference.tearDown();
		assertEquals(CodeReference.getNumberCodes(), 0);
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		assertEquals(CodeReference.getNumberCodes(), 3);
		CodeReference.tearDown();
		assertEquals(CodeReference.getNumberCodes(), 0);
	}
	
	@Test
	void testGetCodeInfo() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		CodeInfo ci = CodeReference.getCodeInfo(15);
		assertNotEquals(ci, null);
		assertEquals(ci.getCode(), 15);
		ci = CodeReference.getCodeInfo(88888);
		assertEquals(ci.getCode(), 88888);
		assertEquals(ci.getImagePath(), CodeReference.DEFAULT_BACKUP_PATH);
		assertEquals(ci.getLabel(), "?");
	}
	
	@Test
	void testGetCodeImagePath() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		CodeInfo ci = CodeReference.getCodeInfo(15);
		assertNotEquals(ci, null);
		assertEquals(ci.getImagePath(), "/assets/add_layer_icon.png");
		ci = CodeReference.getCodeInfo(25);
		assertEquals(ci.getImagePath(), CodeReference.DEFAULT_BACKUP_PATH);
	}
	
	@Test
	void testGetCodeLabel() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		CodeInfo ci = CodeReference.getCodeInfo(15);
		assertNotEquals(ci, null);
		assertEquals(ci.getLabel(), "example code reference value");
	}
	
	@Test
	void testGetCodeTooltips() {
		CodeReference.tearDown();
		assertDoesNotThrow(() -> {CodeReference.setup("src/assets/test_setup.txt");});
		HashMap<Integer, String> tooltips = CodeReference.getCodeTooltips();
		assertEquals("example code reference value", tooltips.get(15));
		assertEquals("another example", tooltips.get(20));
		assertEquals("null example", tooltips.get(25));
	}
	
	

}
