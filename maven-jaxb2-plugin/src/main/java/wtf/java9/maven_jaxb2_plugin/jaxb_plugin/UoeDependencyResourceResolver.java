package wtf.java9.maven_jaxb2_plugin.jaxb_plugin;

import java.net.URL;

/**
 * To prevent inclusion of the entire plugin code, this {@link DependencyResourceResolver} does not do anything except
 * throwing an exception. That code is never reached, though, so this simple project apparently doesn't need this
 * functionality.
 */
public class UoeDependencyResourceResolver implements DependencyResourceResolver {

	@Override
	public URL resolveDependencyResource(DependencyResource dependencyResource) {
		throw new UnsupportedOperationException();
	}

}
