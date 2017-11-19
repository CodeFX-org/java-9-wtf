package wtf.java9.graphics_bounds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class GraphicsBoundsTest {

	@Test
	void createBounds() throws Exception {
		Assertions.assertEquals(
				new Rectangle(0, 0, 800, 600),
				GraphicsBounds.createBounds());
	}
}
