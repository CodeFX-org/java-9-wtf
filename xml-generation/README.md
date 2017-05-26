# Bindings With JAXB2 Plugin

The [Maven JAXB2 Plugin](https://github.com/highsource/maven-jaxb2-plugin) does not [process schema bindings on Java 9](https://github.com/highsource/maven-jaxb2-plugin/issues/120).
This project demonstrates that by running the plugin and afterwards [testing the existence of the expected classes](src/main/test/wtf/java9/xml_generation/JaxbPluginTest.java).

What is irritating is that it was not possible to recreate the error without the plugin!
The [code in `wtf.java9.xml_generation`](src/main/java/wtf/java9/xml_generation) is a simplified (and accidentally "rightified"?) version of the code the plugin is running but it works on both Java 8 and Java 9 as indicated by [another test](src/main/test/wtf/java9/xml_generation/JaxbApiTest.java).
Why?!

## Setup

To verify the behavior on Java 8 you can simply execute `mvn clean test`.
Sources get generated in the both `generated-jaxb-*` folders and all tests pass.

For Java 9, there is a little setup involved:

* execute Maven with Java 9, e.g. by [defining `JAVA_HOME` in a `.mavenrc` file](https://github.com/CodeFX-org/mvn-java-9/tree/master/mavenrc)
* in the project's `.mvn` folder, rename the file `jvm9.config` to `jvm.config`, which adds Java-9-specific command line flags letting the plugin use JDK-internal APIs

Now `mvn clean test` should execute but fail with two unsuccessful tests - these are the ones verifying that the plugin respects the bindings.

## Plugin Version

The published version of this project uses version 0.13.2 of the JAXB2 plugin.
Efforts to make it work with a Java 9 specific version were unsuccessful as well.
Those efforts can be tracked [in this private branch of the Maven JAXB2 Plugin](https://github.com/nicolaiparlog/maven-jaxb2-plugin/tree/java-9).

## API Use

In an effort to slowly approach the plugin's implementation, particularly regarding the used `EntityResolver` different variants were implemented.
They can be seen in [`EntityResovlerFactory`](src/main/java/wtf/java9/xml_generation/EntityResolverFactory.java) and selected with the Maven property `entity.resolver`, e.g.:

    mvn clean test -Dentity.resolver=simple

By default, the plugin's approach is copied as closely as possible.
