package wtf.java9.xml_generation;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.Language;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.api.SpecVersion;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GenerateWithJaxbApi {

	static final Path SCHEMA_FOLDER = FileSystems.getDefault().getPath("schema");
	static final Path GENERATION_TARGET_FOLDER = Paths.get("generated-jaxb-api");

	private static final SystemOutErrorReceiver ERROR_RECEIVER = new SystemOutErrorReceiver();

	// *********
	// EXECUTION
	// *********

	public static void main(String[] args) throws Exception {
		String configuration = args.length == 0 ? "" : args[0];
		new GenerateWithJaxbApi().execute(configuration);
	}

	private void execute(String configuration) throws Exception {
		Options options = createOptions(configuration);
		Model model = ModelLoader.load(options, new JCodeModel(), ERROR_RECEIVER);
		Outline outline = model.generateCode(model.options, ERROR_RECEIVER);
		writeCode(outline);
	}

	private Options createOptions(String configuration) throws IOException {
		Options options = new Options();

		options.compatibilityMode = 2;
		options.disableXmlSecurity = true;
		options.entityResolver = EntityResolverFactory.create(configuration);
		generateSchemaInputSources().forEach(options::addGrammar);
		options.setSchemaLanguage(Language.XMLSCHEMA);
		options.target = SpecVersion.V2_2;
		options.targetDir = createCleanTargetDirectory();

		return options;
	}

	private Stream<InputSource> generateSchemaInputSources() {
		return Stream.of("SimpleClass.xsd", "SimpleClassWithOtherName.xsd", "SimpleClassWithPackage.xsd")
				.map(schemaFile -> SCHEMA_FOLDER.resolve(schemaFile).toAbsolutePath().toUri().toString())
				.map(InputSource::new);
	}

	private File createCleanTargetDirectory() throws IOException {
		return Files.createDirectories(GENERATION_TARGET_FOLDER).toFile();
	}

	private void writeCode(Outline outline) throws IOException {
		Model model = outline.getModel();
		JCodeModel codeModel = model.codeModel;
		codeModel.build(model.options.createCodeWriter());
	}

	private static class SystemOutErrorReceiver extends ErrorReceiver {

		public void error(Exception exception) {
			System.err.println("ERROR");
			exception.printStackTrace();
		}

		@Override
		public void error(SAXParseException exception) {
			System.err.println("ERROR");
			exception.printStackTrace();
		}

		public void fatalError(Exception exception) {
			System.err.println("FATAL ERROR");
			exception.printStackTrace();
		}

		@Override
		public void fatalError(SAXParseException exception) {
			System.err.println("FATAL ERROR");
			exception.printStackTrace();
		}

		public void warning(Exception exception) {
			System.out.println("WARN");
			exception.printStackTrace();
		}

		@Override
		public void warning(SAXParseException exception) {
			System.out.println("WARN");
			exception.printStackTrace();
		}

		public void info(Exception exception) {
			System.out.println("INFO");
			exception.printStackTrace();
		}

		@Override
		public void info(SAXParseException exception) {
			System.out.println("INFO");
			exception.printStackTrace();
		}

	}

}
