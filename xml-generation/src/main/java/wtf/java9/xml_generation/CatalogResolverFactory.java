package wtf.java9.xml_generation;

import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;

public class CatalogResolverFactory {

	public static CatalogResolver createCatalogResolver() {
		final CatalogManager catalogManager = new CatalogManager();
		catalogManager.setIgnoreMissingProperties(true);
		catalogManager.setUseStaticCatalog(false);
		// TODO: maybe replace with org.jvnet.jaxb2.maven2.resolver.tools.MavenCatalogResolver ?
		return new CatalogResolver(catalogManager);
	}

}
