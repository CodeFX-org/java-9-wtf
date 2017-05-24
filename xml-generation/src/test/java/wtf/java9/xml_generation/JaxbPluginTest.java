package wtf.java9.xml_generation;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JaxbPluginTest {

	@Test
	public void simpleCaseWorks() throws Exception {
		Object simpleClass = Class.forName("wtf.java9.xml_generation.SimpleClass").newInstance();
		assertNotNull(simpleClass);
	}

	@Test
	public void packageBindingRespected() throws Exception {
		Object packageBinding = Class.forName("wtf.java9.xml_generation.with.pack.SimpleClassWithPackage").newInstance();
		assertNotNull(packageBinding);
	}

	@Test
	public void classNameBindingRespected() throws Exception {
		Object classNameBinding = Class.forName("wtf.java9.xml_generation.SimpleClassWithRightName").newInstance();
		assertNotNull(classNameBinding);
	}

}
