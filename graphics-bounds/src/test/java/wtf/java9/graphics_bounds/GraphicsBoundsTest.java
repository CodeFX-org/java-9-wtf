package wtf.java9.graphics_bounds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.assertj.core.api.Assertions.assertThat;

class GraphicsBoundsTest {

	@Test
	void createBounds() throws Exception {
		assertThat(GraphicsBounds.createBounds())
				.isEqualTo(new Rectangle(0, 0, 800, 600));
	}
}
