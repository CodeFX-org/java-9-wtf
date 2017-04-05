package wtf.java9.inference;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class GenericArray<T> {

	private Stream<T[]> streamSelectionPaths() {
		Object[][] things2d = new Object[0][0];
		return Arrays
				.stream(things2d)
//				.map(thing -> (Optional<T>[]) lift(thing)) // pass
				.map(GenericArray::lift) // fail
				.map(GenericArray::flatten);
	}

	public static <I> Optional[] lift(I[] array) {
		return Arrays.stream(array).map(Optional::ofNullable).toArray(Optional[]::new);
	}

	public static <T> T[] flatten(Optional<T>[] array) {
		return (T[]) Arrays.stream(array).map(e -> e.orElse(null)).toArray();
	}

}
