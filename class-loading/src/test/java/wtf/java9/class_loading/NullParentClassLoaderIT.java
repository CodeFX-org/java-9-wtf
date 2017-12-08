package wtf.java9.class_loading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class NullParentClassLoaderIT {

	/**
	 * Attempt to load a class from the URLClassLoader that references a java.sql.* class. The
	 * java.sql.* package is one of those not visible to the Java 9 bootstrap class loader that
	 * was visible to the Java 8 bootstrap class loader.
	 */
	@Test
	public void loadSqlDateUsingNullParent(TestReporter reporter) throws Exception {
		URLClassLoader loader = buildLoader(reporter);

		Class<?> jsqlUserClass = loader.loadClass("wtf.java9.class_loading.JavaSqlUser");
		reporter.publishEntry("Loaded class", jsqlUserClass.toString());

		Object jsqlUser = jsqlUserClass.getConstructor().newInstance();
		reporter.publishEntry("Created instance", jsqlUser.toString());

		loader.close();
	}

	/**
	 * Build a URLClassLoader that only includes the null-parent-classloader artifact in its classpath,
	 * along with the bootstrap class loader as its parent.
	 *
	 * @throws MalformedURLException
	 * 		on failure to build the classpath URL
	 */
	private URLClassLoader buildLoader(TestReporter reporter) throws MalformedURLException {
		String testRootFolder = NullParentClassLoaderIT.class.getResource("/").getPath();
		reporter.publishEntry("Test classes folder", testRootFolder);
		Path projectJar = Paths
				.get(testRootFolder)
				.getParent()
				.resolve("class-loading-1.0-SNAPSHOT.jar");

		assumeTrue(
				projectJar.toFile().exists(),
				"Project JAR must exist as " + projectJar.toString() + " for test to be executed.");
		reporter.publishEntry("Project JAR", projectJar.toString());

		URL path[] = { projectJar.toUri().toURL() };
		// this is the parent that is required when running under Java 9:
		// ClassLoader parent = ClassLoader.getPlatformClassLoader();
		ClassLoader parent = null;
		URLClassLoader loader = new URLClassLoader(path, parent);
		reporter.publishEntry("Class loader", loader.toString());

		return loader;
	}

}

