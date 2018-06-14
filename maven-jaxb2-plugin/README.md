# Bindings With Maven JAXB2 Plugin

The [Maven JAXB2 Plugin](https://github.com/highsource/maven-jaxb2-plugin) does not [process schema bindings on Java 9](https://github.com/highsource/maven-jaxb2-plugin/issues/120).
This can be demonstrated that by running the plugin and afterwards [testing the existence of the expected classes](src/test/java/wtf/java9/maven_jaxb2_plugin/JaxbPluginTest.java).

What is irritating is that it was not possible to recreate the error without the plugin!
The [code in `wtf.java9.maven_jaxb2_plugin`](src/main/java/wtf/java9/maven_jaxb2_plugin) is a simplified (and accidentally "rightified"?) version of the code the plugin is running but it works on both Java 8 and Java 9 as indicated by [another test](src/test/java/wtf/java9/maven_jaxb2_plugin/JaxbApiTest.java).
Why?!

(Maybe the dependencies play a role?
Class path content between running the plugin and calling the API differs.)

## Observe!

Running the project with `mvn clean test` creates two `generated-jaxb-*` folders, one for the sources created by the plugin the other by the JAXB API.
On Java 8 they are identical, on Java 9 they are not.
[The tests](src/test/java/wtf/java9/maven_jaxb2_plugin) try to verify that the sources are present and accordingly fail on Java 9.

(Last checked: 8u152 and 9.0.1)

## Plugin Version

The published version of this project uses version 0.13.2 of the JAXB2 plugin.
Efforts to make it work with a Java 9 specific version were unsuccessful as well.
Those efforts can be tracked [in this private branch of the Maven JAXB2 Plugin](https://github.com/nicolaiparlog/maven-jaxb2-plugin/tree/java-9).

**NOTE**: A Java 9 compatible version was released: 0.14.0. Huzzah!

## API Use

In an effort to slowly approach the plugin's implementation, particularly regarding the used `EntityResolver` different variants were implemented.
They can be seen in [`EntityResovlerFactory`](src/main/java/wtf/java9/maven_jaxb2_plugin/EntityResolverFactory.java) and selected with the Maven property `entity.resolver`, e.g.:

    mvn clean test -Dentity.resolver=simple

By default, the plugin's approach is copied as closely as possible.
