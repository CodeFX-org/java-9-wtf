package wtf.java9.maven_jaxb2_plugin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxbPluginTest {

	@Test
	public void simpleCaseWorks() throws Exception {
		Object simpleClass = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClass").newInstance();
		assertThat(simpleClass).isNotNull();
	}

	@Test
	public void packageBindingRespected() throws Exception {
		Object packageBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.with.pack.SimpleClassWithPackage").newInstance();
		assertThat(packageBinding).isNotNull();
	}

	@Test
	public void classNameBindingRespected() throws Exception {
		Object classNameBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClassWithRightName").newInstance();
		assertThat(classNameBinding).isNotNull();
	}

}
