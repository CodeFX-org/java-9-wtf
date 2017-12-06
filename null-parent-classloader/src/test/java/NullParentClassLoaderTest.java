import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.jupiter.api.Test;

public class NullParentClassLoaderTest {

	/**
	 * Attempt to load a class from the URLClassLoader that references a java.sql.* class. The
	 * java.sql.* package is one of those not visible to the Java 9 bootstrap class loader that
	 * was visible to the Java 8 bootstrap class loader.
	 * @throws Exception
	 */
	@Test
	public void loadSqlDateUsingNullParent() throws Exception {
		URLClassLoader loader = buildLoader();
		Class<?> jsqlUserClass = loader.loadClass("JavaSqlUser");
		System.out.printf("Loaded class: %s, loader=%s\n", jsqlUserClass, jsqlUserClass.getClassLoader());
		Object jsqlUser = jsqlUserClass.getConstructor().newInstance();
		System.out.printf("Loaded instance: %s\n", jsqlUser);
		loader.close();
	}

	public static void main(String[] args) throws Exception {
		NullParentClassLoaderTest test = new NullParentClassLoaderTest();
		test.loadSqlDateUsingNullParent();
	}

	/**
	 * Build a URLClassLoader that only includes the null-parent-classloader artifact in its classpath,
	 * along with the bootstrap class loader as its parent.
	 * @return a URLClassLoader
	 * @throws MalformedURLException on failure to build the classpath URL
	 */
	private URLClassLoader buildLoader() throws MalformedURLException {
		URL rootURL = NullParentClassLoaderTest.class.getResource("/");
		System.out.printf("rootURL=%s, file=%s\n", rootURL, rootURL.getFile());
		// The target/test-classes directory
		File testClasses = new File(rootURL.getFile());
		// The target directory containing the null-parent-classloader artifact
		File targetDir = testClasses.getParentFile();
		URL appJar = new URL(targetDir.toURI().toURL(), "null-parent-classloader-1.0-SNAPSHOT.jar");
		URL path[] = {appJar};
		ClassLoader parent = null;
		// this is the parent that is required when running under Java 9
		//ClassLoader parent = ClassLoader.getPlatformClassLoader();
		URLClassLoader loader = new URLClassLoader(path, parent);
		return loader;
	}
}

