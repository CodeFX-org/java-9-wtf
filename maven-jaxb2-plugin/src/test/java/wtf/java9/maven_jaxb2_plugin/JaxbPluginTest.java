package wtf.java9.maven_jaxb2_plugin;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JaxbPluginTest {

	@Test
	public void simpleCaseWorks() throws Exception {
		Object simpleClass = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClass").newInstance();
		assertNotNull(simpleClass);
	}

	@Test
	public void packageBindingRespected() throws Exception {
		Object packageBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.with.pack.SimpleClassWithPackage").newInstance();
		assertNotNull(packageBinding);
	}

	@Test
	public void classNameBindingRespected() throws Exception {
		Object classNameBinding = Class.forName("wtf.java9.maven_jaxb2_plugin.SimpleClassWithRightName").newInstance();
		assertNotNull(classNameBinding);
	}

}
