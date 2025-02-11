package control.code;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CodeInfoTest {

	@Test
	void testGetters() {
		CodeInfo ci = new CodeInfo(5, "/assets/add_layer_icon.png", "test_label");
		assertEquals(ci.getCode(), 5);
		assertEquals(ci.getImagePath(), "/assets/add_layer_icon.png");
		assertEquals(ci.getLabel(), "test_label");
		CodeInfo ci2 = new CodeInfo(15, null, "test_two");
		assertEquals(ci2.getCode(), 15);
		assertEquals(ci2.getImagePath(), CodeInfo.DEFAULT_IMAGE);
		// TODO: Should this reference the constant in CodeInfo or be the root value?
		assertEquals(ci2.getLabel(), "test_two");
	}

}
