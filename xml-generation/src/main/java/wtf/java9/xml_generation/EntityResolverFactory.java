package wtf.java9.xml_generation;

import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import org.xml.sax.EntityResolver;
import wtf.java9.xml_generation.jaxb_plugin.MavenCatalogResolver;
import wtf.java9.xml_generation.jaxb_plugin.ReResolvingEntityResolverWrapper;
import wtf.java9.xml_generation.jaxb_plugin.UoeDependencyResourceResolver;

public class EntityResolverFactory {

	public static EntityResolver create(String configuration) {
		switch (configuration) {
			case "blank":
				return blank();
			case "simple":
				return withDefaultSimpleCatalogManager();
			case "simpleConfigured":
				return withConfiguredSimpleCatalogManager();
			case "":
			case "asPlugin":
				return asInPlugin();
			default:
				throw new IllegalArgumentException("Unknown configuration: " + configuration);
		}
	}

	private static EntityResolver blank() {
		// works on Java 9
		return new CatalogResolver();
	}

	private static EntityResolver withDefaultSimpleCatalogManager() {
		// works on Java 9
		return new CatalogResolver(new CatalogManager());
	}

	private static EntityResolver withConfiguredSimpleCatalogManager() {
		// works on Java 9
		final CatalogManager catalogManager = new CatalogManager();
		catalogManager.setIgnoreMissingProperties(true);
		catalogManager.setUseStaticCatalog(false);
		return new CatalogResolver(catalogManager);
	}

	private static EntityResolver asInPlugin() {
		// works on Java 9 - WHY?!
		final CatalogManager catalogManager = new CatalogManager();
		catalogManager.setIgnoreMissingProperties(true);
		catalogManager.setUseStaticCatalog(false);
		MavenCatalogResolver catalogResolver = new MavenCatalogResolver(
				catalogManager,
				new UoeDependencyResourceResolver());
		return new ReResolvingEntityResolverWrapper(catalogResolver);
	}

}
