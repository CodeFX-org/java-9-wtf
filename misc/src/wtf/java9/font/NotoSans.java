package wtf.java9.font;

import sun.font.Font2D;
import sun.font.FontUtilities;

import java.awt.*;
import java.lang.reflect.Constructor;

public class NotoSans {

	public static void main(String[] args) throws ReflectiveOperationException {
		// these APIs are used to reproduce the specific case I encountered
		// as closely as possible
		Font2D font = FontUtilities.getFont2D(new Font("Noto Sans CJK JP Black", 0, 12));

		// this is a reconstruction of what happens at the end of a call stack like:
		//  - BasicListUI.updateLayoutState()
		//  - JComponent.getPreferredSize()
		//  - JComponent.getFontMetrics(Font)
		//  - TrueTypeFont.getScaler
		Constructor<?> constructor = Class
				.forName("sun.font.T2KFontScaler")
				.getConstructor(Font2D.class, int.class, boolean.class, int.class);
		constructor.setAccessible(true);
		Object scaler = constructor.newInstance(font, 0, true, 18604592);

		System.out.println(scaler);
	}

}
