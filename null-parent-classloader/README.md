# Null Parent ClassLoader Used With URLClassLoader No Longer Has Full Platform Visibility

A common pattern in server and testing frameworks is the loading of external code via a URLClassLoader with a null
parent ClassLoader to isolate the loaded coded to just the classes of the runtime Java platform and the classpath
passed to the URLClassLoader. A null parent ClassLoader signifies the bootstrap ClassLoader. The set of classes
that the bootstrap ClassLoader is able to load has changed between Java 8 and Java 9. In Java 8 every platform
class other than the JavaFX classes is visible to the bootstrap ClassLoader. Under Java 9, the bootstrap
ClassLoader has reduced this. The list of modules that go into the
bootstrap class loader can be found in [JEP 261: Module System#Class-loaders](http://openjdk.java.net/jeps/261#Class-loaders).
That defines the bootstrap class loader to consist of the following modules:
* java.base
* java.datatransfer
* java.desktop
* java.instrument
* java.logging
* java.management
* java.management.rmi
* java.naming
* java.prefs
* java.rmi
* java.security.sasl
* java.xml
* jdk.httpserver
* jdk.internal.vm.ci
* jdk.management
* jdk.management.agent
* jdk.naming.rmi
* jdk.net
* jdk.sctp
* jdk.unsupported

I have created a `BootstrapLoaderTest` that attempts to load classes from several of the
non-core java/javax packages to validate what packages are visible to the bootstrap class loader.

This shows the following packages that were visible under Java 8 to no longer be visible under Java 9:
* java.sql.*
* javax.activation.*
* javax.annotation.*
* javax.jws.*
* javax.lang.model.*
* javax.rmi.*
* javax.script.*
* javax.smartcardio.*
* javax.sql.*
* javax.tools.*
* javax.transaction.xa.*
* javax.xml.bind.*
* javax.xml.crypto.*
* javax.xml.soap.*
* javax.xml.ws.*


To build and run the NullParentClassLoaderTest tests, use:

```
mvn package -DskipTests=true
mvn test
```

Output on Java 8:

```
Running BootstrapLoaderTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.092 sec - in BootstrapLoaderTest
Running NullParentClassLoaderTest
rootURL=file:/Users/starksm/Dev/Java/Java9/starksm64-java-9-wtf/null-parent-classloader/target/test-classes/, file=/Users/starksm/Dev/Java/Java9/starksm64-java-9-wtf/null-parent-classloader/target/test-classes/
Loaded class: class JavaSqlUser, loader=java.net.URLClassLoader@53f65459
Loaded instance: JavaSqlUser(SqlDateUser@15d0c81b)
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in NullParentClassLoaderTest

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

Under Java 9, this is no longer works and instead the new [ClassLoader#getPlatformClassLoader](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#getPlatformClassLoader--),
method must be used to obtain a parent that has visibility to all of the platform classes. Using null as the parent under Java 9 causes:

```
Running BootstrapLoaderTest
java.sql.Date is not visible
javax.activation.DataHandler is not visible
javax.annotation.PostConstruct is not visible
javax.jws.WebMethod is not visible
javax.lang.model.SourceVersion is not visible
javax.rmi.PortableRemoteObject is not visible
javax.script.AbstractScriptEngine is not visible
javax.smartcardio.Card is not visible
javax.sql.XADataSource is not visible
javax.tools.JavaCompiler is not visible
javax.transaction.xa.XAResource is not visible
javax.xml.bind.Element is not visible
javax.xml.bind.annotation.XmlElement is not visible
javax.xml.crypto.XMLStructure is not visible
javax.xml.soap.Name is not visible
javax.xml.ws.Service is not visible
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.102 sec <<< FAILURE! - in BootstrapLoaderTest
testLoadClasses()  Time elapsed: 0.075 sec  <<< FAILURE!
org.junit.ComparisonFailure: expected:<[0]> but was:<[16]>
	at BootstrapLoaderTest.testLoadClasses(BootstrapLoaderTest.java:81)

Running NullParentClassLoaderTest
rootURL=file:/Users/starksm/Dev/Java/Java9/starksm64-java-9-wtf/null-parent-classloader/target/test-classes/, file=/Users/starksm/Dev/Java/Java9/starksm64-java-9-wtf/null-parent-classloader/target/test-classes/
Loaded class: class JavaSqlUser, loader=java.net.URLClassLoader@128d2484
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.002 sec <<< FAILURE! - in NullParentClassLoaderTest
loadSqlDateUsingNullParent()  Time elapsed: 0.002 sec  <<< FAILURE!
java.lang.NoClassDefFoundError: java/sql/Date
	at NullParentClassLoaderTest.loadSqlDateUsingNullParent(NullParentClassLoaderTest.java:21)
Caused by: java.lang.ClassNotFoundException: java.sql.Date
	at NullParentClassLoaderTest.loadSqlDateUsingNullParent(NullParentClassLoaderTest.java:21)


Results :

Failed tests:
  BootstrapLoaderTest.testLoadClasses:81 expected:<[0]> but was:<[16]>
  NullParentClassLoaderTest.loadSqlDateUsingNullParent:21 » NoClassDefFound java...

Tests run: 2, Failures: 2, Errors: 0, Skipped: 0
```

## Workaround
The workaround for the failure in the `NullParentClassLoaderTest` is to pass in the new Java 9 platform classloader
available from the `ClassLoader#getPlatformClassLoader()` method when building the URLClassLoader:
```java
    URL path[] = {...};
    ClassLoader parent = ClassLoader.getPlatformClassLoader();
    URLClassLoader loader = new URLClassLoader(path, parent);
```

(Last checked: 8u152 and 9.0.1; contributed by [Scott Stark](https://github.com/starksm64))
