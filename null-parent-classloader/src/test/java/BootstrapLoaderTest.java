import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A simple utility class that validates some of the key java.* and javax.* package class visibility
 * from the bootstrap ClassLoader
 */
public class BootstrapLoaderTest {

	@ParameterizedTest(name = "loading {0}")
	@MethodSource(value = "classNames")
	public void loadJdkClass(String className, TestReporter reporter) throws ClassNotFoundException {
		TestClassLoader classLoader = new TestClassLoader();

		try {
			Class c = classLoader.loadClass(className);
			reporter.publishEntry(className, "visible");
			// the assertion is pretty useless, but if `c` would not be used,
			// dead code elimination might remove it
			assertThat(c.getName()).isEqualTo(className);
		} catch (ClassNotFoundException ex) {
			reporter.publishEntry(className, "not visible");
			throw ex;
		}
	}

	private static Stream<String> classNames() {
		return Stream.of(
				"java.applet.Applet",
				"java.awt.Image",
				"java.awt.dnd.DropTarget",
				"java.awt.print.Paper",
				"java.beans.BeanInfo",
				"java.lang.instrument.ClassFileTransformer",
				"java.lang.management.ClassLoadingMXBean",
				"java.sql.Date",
				"java.net.ServerSocket",
				"java.rmi.Naming",
				"java.rmi.activation.Activator",
				"java.security.acl.Acl",
				"java.text.spi.NumberFormatProvider",
				"java.util.logging.Logger",
				"java.util.prefs.Preferences",
				"java.util.zip.ZipFile",
				"javax.activation.DataHandler",
				"javax.annotation.PostConstruct",
				"javax.imageio.ImageIO",
				"javax.jws.WebMethod",
				"javax.lang.model.SourceVersion",
				"javax.management.JMX",
				"javax.naming.Context",
				"javax.net.SocketFactory",
				"javax.print.PrintService",
				"javax.rmi.PortableRemoteObject",
				"javax.script.AbstractScriptEngine",
				"javax.security.cert.X509Certificate",
				"javax.smartcardio.Card",
				"javax.sound.midi.MidiDevice",
				"javax.sql.XADataSource",
				"javax.swing.SwingUtilities",
				"javax.tools.JavaCompiler",
				"javax.transaction.xa.XAResource",
				"javax.xml.XMLConstants",
				"javax.xml.bind.Element",
				"javax.xml.bind.annotation.XmlElement",
				"javax.xml.crypto.XMLStructure",
				"javax.xml.datatype.DatatypeConstants",
				"javax.xml.namespace.QName",
				"javax.xml.parsers.DocumentBuilder",
				"javax.xml.soap.Name",
				"javax.xml.stream.XMLStreamConstants",
				"javax.xml.transform.Transformer",
				"javax.xml.validation.Validator",
				"javax.xml.ws.Service",
				"javax.xml.xpath.XPath"
		);
	}

	/**
	 * A simple ClassLoader that specifies null to use the bootstrap ClassLoader as its parent
	 */
	static class TestClassLoader extends ClassLoader {

		TestClassLoader() {
			super(null);
		}
	}

}
