---
title: Class Loading
date: 2017-12-05
---


There are several changes with regards to class loading, but so far we've only documented one.

## Restricted Bootstrap Class Loader

A common requirement in server and testing frameworks is the isolation of external code from the framework.
Before Java 9, this could be done by loading the external code via a `URLClassLoader` with a `null` parent class loader, which limits it to just the classes of the runtime Java platform and the class path passed to the `URLClassLoader`.
A `null` parent class loader signifies the bootstrap class loader.

The set of classes that the bootstrap class loader is able to load has changed between Java 8 and Java 9:

* In Java 8, every platform class other than the JavaFX classes is visible to the bootstrap class loader.
* In Java 9, the bootstrap class loader is more restricted.

The list of modules that go into the bootstrap class loader can be found in [JEP 261](http://openjdk.java.net/jeps/261#Class-loaders):

* _java.base_
* _java.datatransfer_
* _java.desktop_
* _java.instrument_
* _java.logging_
* _java.management_
* _java.management.rmi_
* _java.naming_
* _java.prefs_
* _java.rmi_
* _java.security.sasl_
* _java.xml_
* _jdk.httpserver_
* _jdk.internal.vm.ci_
* _jdk.management_
* _jdk.management.agent_
* _jdk.naming.rmi_
* _jdk.net_
* _jdk.sctp_
* _jdk.unsupported_

As a demonstration, [`BootstrapLoaderTest`](https://github.com/CodeFX-org/java-9-wtf/tree/master/./class-loading/src/test/java/wtf/java9/class_loading/BootstrapLoaderTest.java) attempts to load classes from several of the non-core java/javax packages to validate what packages are visible to the bootstrap class loader.
This shows the following packages that were visible under Java 8 to no longer be visible under Java 9:

* `java.sql.*`
* `javax.activation.*`
* `javax.annotation.*`
* `javax.jws.*`
* `javax.lang.model.*`
* `javax.rmi.*`
* `javax.script.*`
* `javax.smartcardio.*`
* `javax.sql.*`
* `javax.tools.*`
* `javax.transaction.xa.*`
* `javax.xml.bind.*`
* `javax.xml.crypto.*`
* `javax.xml.soap.*`
* `javax.xml.ws.*`

[`NullParentClassLoaderIT`](https://github.com/CodeFX-org/java-9-wtf/tree/master/./class-loading/src/test/java/wtf/java9/class_loading/NullParentClassLoaderIT.java) shows the consequences by trying to load a class from the project JAR that uses `java.sql.Date`, which is among the invisible packages.

To observe that, you need to successfully build the JAR, either by building on Java 8 or running `mvn package -DskipTests` on Java 9 (otherwise `BootstrapLoaderTest` fails your build).
Either way, you can then run the test with `mvn failsafe:integration-test`.

### Solution

On Java 9, the new [ClassLoader::getPlatformClassLoader](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#getPlatformClassLoader--), method must be used to obtain a parent that has visibility to all of the platform classes.
It can then be passed to the `URLClassLoader`:

```java
URL path[] = { ... };
ClassLoader parent = ClassLoader.getPlatformClassLoader();
URLClassLoader loader = new URLClassLoader(path, parent);
```

(Last checked: 8u152 and 9.0.1; contributed by [Scott Stark](https://github.com/starksm64))
