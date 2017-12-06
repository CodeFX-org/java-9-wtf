# Null Parent ClassLoader Used With URLClassLoader No Longer Has Full Platform Visibility

In Java 8 if one passes in a null value as the parent of a URLClassLoader, one is able to load any class in
the Java platform. However, under Java 9, such a URLClassLoader is only able to load classes from the
java.base module.

Output on Java 8:

```
Running NullParentClassLoaderTest
rootURL=file:/Users/starksm/Dev/Java/Java9/java-9-wtf/null-parent-classloader/target/test-classes/, file=/Users/starksm/Dev/Java/Java9/java-9-wtf/null-parent-classloader/target/test-classes/
Loaded class: class JavaSqlUser, loader=java.net.URLClassLoader@cb5822
Loaded instance: JavaSqlUser(SqlDateUser@1d057a39)
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.056 sec - in NullParentClassLoaderTest
```

In Java 9, this is no longer works and instead the new [ClassLoader#getPlatformClassLoader](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#getPlatformClassLoader--), method
must be used to obtain a parent that has visibility to all of the platform classes. Using null as the parent under Java 9 causes:

```
Running NullParentClassLoaderTest
rootURL=file:/Users/starksm/Dev/Java/Java9/java-9-wtf/null-parent-classloader/target/test-classes/, file=/Users/starksm/Dev/Java/Java9/java-9-wtf/null-parent-classloader/target/test-classes/
Loaded class: class JavaSqlUser, loader=java.net.URLClassLoader@43301423
java.lang.NoClassDefFoundError: java/sql/Date
	at NullParentClassLoaderTest.loadUsingNullParent(NullParentClassLoaderTest.java:22)
Caused by: java.lang.ClassNotFoundException: java.sql.Date
	at NullParentClassLoaderTest.loadUsingNullParent(NullParentClassLoaderTest.java:22)
```

(Last checked: 8u152 and 9.0.1; contributed by [Scott Stark](https://github.com/starksm64))
