package wtf.java9.xml_generation;

import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import org.xml.sax.EntityResolver;

public class EntityResolverFactory {

	public static EntityResolver create(String configuration) {
		switch (configuration) {
			case "":
			case "blank": return blank();
			case "simple": return withDefaultSimpleCatalogManager();
			case "simpleConfigured": return withConfiguredSimpleCatalogManager();
			case "9yards": return asInPlugin();
			default: throw new IllegalArgumentException("Unknown configuration: " + configuration);
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
		// TODO: works on Java 9?
		final CatalogManager catalogManager = new CatalogManager();
		catalogManager.setIgnoreMissingProperties(true);
		catalogManager.setUseStaticCatalog(false);
		// TODO: replace with org.jvnet.jaxb2.maven2.resolver.tools.MavenCatalogResolver
		// TODO: wrap into ReResolvingEntityResolverWrapper
		return new CatalogResolver(catalogManager);
	}

}
