package wtf.java9.graphics_bounds;

import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.Constructor;

public class GraphicsBounds {

	public static void main(String[] args) {
		System.out.println(createBounds());
	}

	/**
	 * Should return 800x600, right?
	 */
	public static Rectangle createBounds() {
		return new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR)
			.createGraphics().getDeviceConfiguration().getBounds();
	}
}
