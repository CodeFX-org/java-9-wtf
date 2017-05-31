package wtf.java9.maven_jaxb2_plugin;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static wtf.java9.maven_jaxb2_plugin.GenerateWithJaxbApi.GENERATION_TARGET_FOLDER;

public class JaxbApiTest {

	@Test
	public void simpleCaseWorks() throws Exception {
		Path simpleClass = Paths
				.get("wtf", "java9", "maven_jaxb2_plugin", "SimpleClass.java");
		File file = GENERATION_TARGET_FOLDER.resolve(simpleClass).toFile();

		assertThat(file).exists();
	}

	@Test
	public void packageBindingRespected() throws Exception {
		Path packageNameBinding = Paths
				.get("wtf", "java9", "maven_jaxb2_plugin", "with", "pack", "SimpleClassWithPackage.java");
		File file = GENERATION_TARGET_FOLDER.resolve(packageNameBinding).toFile();

		assertThat(file).exists();
	}

	@Test
	public void classNameBindingRespected() throws Exception {
		Path classNameBinding = Paths
				.get("wtf", "java9", "maven_jaxb2_plugin", "SimpleClassWithRightName.java");
		File file = GENERATION_TARGET_FOLDER.resolve(classNameBinding).toFile();

		assertThat(file).exists();
	}

}
