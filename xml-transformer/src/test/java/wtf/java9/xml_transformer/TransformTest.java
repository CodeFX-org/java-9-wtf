package wtf.java9.xml_transformer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TransformTest {

	// note that the indentation is two spaces
	private static final String INITIAL_XML =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>\n" +
			"  <node></node>\n" +
			"</root>";

	/*
	 * To experiment with `xml:space="preserve"` add it to the root element:
	 *
	 * On Java 8 it has no impact, so both tests suites should pass just the same.
	 * Apparently it is overridden by the transformer's configuration.
	 *
	 * On Java 9 it makes the configured case behave like the default one, so
	 * apparently it overrides the transformer's configuration.
	 */

	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	private Transformer transformer;

	@BeforeEach
	void createTransformer() throws Exception {
		transformer = TRANSFORMER_FACTORY.newTransformer();
	}

	@Nested
	class DefaultSettings {

		@Test
		void keepsRootOnFirstLine() throws Exception {
			Lines transformation = transform(parse(INITIAL_XML));

			assertThat(transformation.at(0)).contains("<root");
		}

		@Test
		void keepsIndentation() throws Exception {
			Lines transformation = transform(parse(INITIAL_XML));

			assertThat(transformation.at(1)).startsWith("  ");
		}

		@Test
		void newNodesAreInline() throws Exception {
			Document document = parse(INITIAL_XML);
			setChildNode(document, "node", "inner", "inner node content");
			Lines transformation = transform(document);

			assertThat(transformation.at(1)).endsWith("<node><inner>inner node content</inner></node>");
		}

		@Test
		void cDataIsInline() throws Exception {
			Document document = parse(INITIAL_XML);
			setCDataContent(document, "node", "cdata content");
			Lines transformation = transform(document);

			assertThat(transformation.at(1)).endsWith("<node><![CDATA[cdata content]]></node>");
		}

	}

	@Nested
	class IndentationSettings {

		@BeforeEach
		void createTransformer() throws Exception {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// note that the indentation is set to be changed to four spaces
			// TODO why is indent 4 not used in Java 9? (looks like 2)
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
		}

		@Test
		void pushesRootNodeToUnindentedNewLine() throws Exception {
			Lines transformation = transform(parse(INITIAL_XML));

			assertThat(transformation.at(0)).doesNotContain("<root");
			assertThat(transformation.at(1)).startsWith("<root");

		}

		@Test
		void keepsIndentationOfUnchangedNodes() throws Exception {
			Lines transformation = transform(parse(INITIAL_XML));

			assertThat(transformation.at(2)).startsWith("  ");
		}

		@Test
		void newNodesAreIndented(TestReporter reporter) throws Exception {
			Document document = parse(INITIAL_XML);
			setChildNode(document, "node", "inner", "inner node content");
			Lines transformation = transform(document);

			reporter.publishEntry("Transformed XML", "\n" + transformation);

			assertThat(transformation.at(2)).endsWith("<node>");
			assertThat(transformation.at(3)).isEqualTo("        <inner>inner node content</inner>");
			assertThat(transformation.at(4)).endsWith("</node>");
		}

		@Test
		void cDataIsInline(TestReporter reporter) throws Exception {
			Document document = parse(INITIAL_XML);
			setCDataContent(document, "node", "cdata content");
			Lines transformation = transform(document);

			reporter.publishEntry("Transformed XML", "\n" + transformation);

			assertThat(transformation.at(2)).endsWith("<node><![CDATA[cdata content]]></node>");
		}

	}

	/*
	 * HELPER
	 */
	
	private static Document parse(String xml) throws Exception {
		return DOCUMENT_BUILDER_FACTORY
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}

	private static void setCDataContent(Document document, String nodeName, String nodeContent) {
		CDATASection cDATASection = document.createCDATASection(nodeContent);
		document.getElementsByTagName(nodeName).item(0)
				.appendChild(cDATASection);
	}

	private static void setChildNode(Document document, String parentName, String childName, String childContent) {
		Element inner = document.createElementNS(null, childName);
		inner.appendChild(document.createTextNode(childContent));
		document.getElementsByTagName(parentName).item(0)
				.appendChild(inner);
	}

	private Lines transform(Document document) throws Exception {
		Source source = new DOMSource(document);
		StringWriter outputWriter = new StringWriter();
		transformer.transform(source, new StreamResult(outputWriter));
		return Lines.of(outputWriter.toString());
	}

	static class Lines {

		private final String[] lines;

		private Lines(String[] lines) {
			this.lines = requireNonNull(lines);
		}

		public static Lines of(String string) {
			return new Lines(string.split(System.lineSeparator()));
		}

		public String at(int line) {
			return lines[line];
		}

		@Override
		public String toString() {
			return String.join("\n", lines);
		}

	}

}
