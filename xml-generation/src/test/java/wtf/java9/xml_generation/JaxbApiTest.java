package wtf.java9.xml_generation;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static wtf.java9.xml_generation.GenerateWithJaxbApi.GENERATION_TARGET_FOLDER;

public class JaxbApiTest {

	@Test
	public void simpleCaseWorks() throws Exception {
		Path simpleClass = Paths
				.get("wtf", "java9", "xml_generation", "SimpleClass.java");
		File file = GENERATION_TARGET_FOLDER.resolve(simpleClass).toFile();

		assertTrue(file.exists());
	}

	@Test
	public void packageBindingRespected() throws Exception {
		Path packageNameBinding = Paths
				.get("wtf", "java9", "xml_generation", "with", "pack", "SimpleClassWithPackage.java");
		File file = GENERATION_TARGET_FOLDER.resolve(packageNameBinding).toFile();

		assertTrue(file.exists());
	}

	@Test
	public void classNameBindingRespected() throws Exception {
		Path classNameBinding = Paths
				.get("wtf", "java9", "xml_generation", "SimpleClassWithRightName.java");
		File file = GENERATION_TARGET_FOLDER.resolve(classNameBinding).toFile();

		assertTrue(file.exists());
	}

}
