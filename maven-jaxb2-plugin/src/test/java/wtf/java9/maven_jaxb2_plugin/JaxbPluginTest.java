package wtf.java9.maven_jaxb2_plugin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxbPluginTest {

	@Test // expected to pass on Java 9
	public void simpleCaseWorks() throws Exception {
		Object simpleClass = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClass").newInstance();
		assertThat(simpleClass).isNotNull();
	}

	@Test // expected to fail on Java 9 because the binding is not processed
	public void packageBindingRespected() throws Exception {
		Object packageBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.with.pack.SimpleClassWithPackage").newInstance();
		assertThat(packageBinding).isNotNull();
	}

	@Test // expected to fail on Java 9 because the binding is not processed
	public void classNameBindingRespected() throws Exception {
		Object classNameBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClassWithRightName").newInstance();
		assertThat(classNameBinding).isNotNull();
	}

}
