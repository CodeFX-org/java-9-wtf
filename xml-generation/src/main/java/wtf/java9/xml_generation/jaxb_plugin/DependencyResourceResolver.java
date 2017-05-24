package wtf.java9.xml_generation.jaxb_plugin;

import java.net.URL;

public interface DependencyResourceResolver {

	URL resolveDependencyResource(DependencyResource dependencyResource);

}
