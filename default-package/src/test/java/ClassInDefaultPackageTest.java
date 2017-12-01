import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassInDefaultPackageTest {

	@Test
	void defaultPackageIsNull() throws Exception {
		assertThat(ClassInDefaultPackageTest.class.getPackage()).isNull();
	}

	public static void main(String[] args) {
		System.out.printf("Class        : '%s'%n", ClassInDefaultPackageTest.class);
		System.out.printf("Package      : '%s'%n", ClassInDefaultPackageTest.class.getPackage());
		System.out.printf("Package name : '%s'%n", packageName());
	}

	private static String packageName() {
		try {
			return ClassInDefaultPackageTest.class.getPackage().getName();
		} catch (NullPointerException ex) {
			return "<NullPointerException>";
		}
	}

}
