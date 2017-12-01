# Default Package Is No Longer `null`

In Java 8 asking a class in the default package for its package [returns null](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#getPackage--):

```java
System.out.printf("Class        : '%s'%n", ClassInDefaultPackageTest.class);
System.out.printf("Package      : '%s'%n", ClassInDefaultPackageTest.class.getPackage());
System.out.printf("Package name : '%s'%n", packageName());
```

Output on Java 8:

```
Class        : 'class ClassInDefaultPackageTest'
Package      : 'null'
Package name : '<NullPointerException>'
```

In Java 9, this is [no longer the case](https://docs.oracle.com/javase/9/docs/api/java/lang/Class.html#getPackage--), instead an instance of `Package` is returned:

```
Class        : 'class ClassInDefaultPackageTest'
Package      : 'package '
Package name : ''
```

(Last checked: 8u152 and 9.0.1; contributed by [Christian Stein](https://github.com/sormuras))
