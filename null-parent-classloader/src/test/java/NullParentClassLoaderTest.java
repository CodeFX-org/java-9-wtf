import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.jupiter.api.Test;

public class NullParentClassLoaderTest {

	@Test
	public void loadUsingNullParent() throws Exception {
		URL rootURL = NullParentClassLoaderTest.class.getResource("/");
		System.out.printf("rootURL=%s, file=%s\n", rootURL, rootURL.getFile());
		File testClasses = new File(rootURL.getFile());
		File targetDir = testClasses.getParentFile();
		URL jsqlJar = new URL(targetDir.toURI().toURL(), "null-parent-classloader-1.0-SNAPSHOT.jar");
		URL path[] = {rootURL, jsqlJar};
		ClassLoader parent = null;
		//ClassLoader parent = ClassLoader.getPlatformClassLoader(); pass by using new Java 9 API to get parent
		URLClassLoader loader = new URLClassLoader(path, parent);
		Class<?> jsqlUserClass = loader.loadClass("JavaSqlUser");
		System.out.printf("Loaded class: %s, loader=%s\n", jsqlUserClass, jsqlUserClass.getClassLoader());
		Object jsqlUser = jsqlUserClass.getConstructor().newInstance();
		System.out.printf("Loaded instance: %s\n", jsqlUser);
		loader.close();
	}

	public static void main(String[] args) throws Exception {
		NullParentClassLoaderTest test = new NullParentClassLoaderTest();
		test.loadUsingNullParent();
	}

}

