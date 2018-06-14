# Tycho / JDT

Contrary to how I understood [the documentation](https://wiki.eclipse.org/JDT_Core/Java9) (particularly [#457413](https://bugs.eclipse.org/bugs/show_bug.cgi?id=457413)), the Eclipse compiler initially didn't properly support Java 9.

The problem described here is fixed in recent releases.

## Unresolved Java EE Modules

[Java EE modules are deprecated for removal and not resolved by default](https://blog.codefx.org/java/java-9-migration-guide/#Dependencies-On-Java-EE-Modules) and [will be removed in Java 11](https://medium.com/codefx-weekly/switch-expressions-and-java-ee-modules-in-java-11-c4efc8811e36#054b).
The best way to handle this is to [use a third-party dependency](https://stackoverflow.com/a/48204154/2525313), but JDT tripped over its own feet when that is done.

This project uses a JAXB class, `javax.xml.bind.JAXBContext`, which can be found in the Jave EE module _java.xml.bind_ and in the artifact _javax.xml.bind:jaxb-api_.
Adding the artifact as a dependency leads to the following error:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.7.0:
	compile (default-compile) on project tycho-jdt:
	Compilation failure
[ERROR] .../java-9-wtf/tycho-jdt/src/main/java/wtf/java9/tycho_jdt/ImportJAXBType.java:
[ERROR] 	package wtf.java9.tycho_jdt;
[ERROR] 	^
[ERROR] Internal compiler error: java.lang.NullPointerException at org.eclipse.jdt.internal.compiler.lookup.BinaryModuleBinding.create(BinaryModuleBinding.java:64)
[ERROR] java.lang.NullPointerException
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.BinaryModuleBinding.create(BinaryModuleBinding.java:64)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment.getModuleFromAnswer(LookupEnvironment.java:423)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment.askForTypeFromModules(LookupEnvironment.java:363)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment.askForType(LookupEnvironment.java:224)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.UnresolvedReferenceBinding.resolve(UnresolvedReferenceBinding.java:105)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.resolveType(BinaryTypeBinding.java:215)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.resolveTypesFor(BinaryTypeBinding.java:1521)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.getExactMethod(BinaryTypeBinding.java:1146)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.Scope.findExactMethod(Scope.java:1274)
[ERROR] 	at org.eclipse.jdt.internal.compiler.lookup.Scope.getMethod(Scope.java:2879)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.MessageSend.findMethodBinding(MessageSend.java:938)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.MessageSend.resolveType(MessageSend.java:759)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.LocalDeclaration.resolve(LocalDeclaration.java:257)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration.resolveStatements(AbstractMethodDeclaration.java:634)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.MethodDeclaration.resolveStatements(MethodDeclaration.java:307)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration.resolve(AbstractMethodDeclaration.java:544)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.TypeDeclaration.resolve(TypeDeclaration.java:1211)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.TypeDeclaration.resolve(TypeDeclaration.java:1324)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration.resolve(CompilationUnitDeclaration.java:625)
[ERROR] 	at org.eclipse.jdt.internal.compiler.Compiler.process(Compiler.java:885)
[ERROR] 	at org.eclipse.jdt.internal.compiler.ProcessTaskManager.run(ProcessTaskManager.java:141)
[ERROR] 	at java.base/java.lang.Thread.run(Thread.java:844)
```

Research lead to the issues [#525522](https://bugs.eclipse.org/bugs/show_bug.cgi?id=525522) and [#526206](https://bugs.eclipse.org/bugs/show_bug.cgi?id=526206), but they are supposedly fixed, so this shouldn't have happened.
Yet it did, so I opened [a new issue](https://bugs.eclipse.org/bugs/show_bug.cgi?id=531579).

After that was resolved it took some time for the fix to show up in a publicly available artifact.
The first artifact I know that contains the fix and works with Java 10 comes with Eclipse Photon I20180531-0700.
Execute the following in Eclipse's `plugins` folder (you may have to update the version):

```
mvn install:install-file \
    -Dfile=org.eclipse.jdt.core_3.14.0.v20180528-0519.jar \
    -DgroupId=org.eclipse.tycho \
    -DartifactId=org.eclipse.jdt.core \
    -Dversion=3.14.0.v20180528-0519 \
    -Dpackaging=jar
```

You can then use that artifact in your build by referencing it as usual - for details, see [the POM](pom.xml).

## Command Line Flags

Unfortunately, the JDT compiler still seems to be unable to process [Java 9's new command line options](https://blog.codefx.org/java/five-command-line-options-to-hack-the-java-9-module-system/), so that removing the dependency and adding `--add-modules=java.xml.bind` didn't work either:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.7.0:
	compile (default-compile) on project tycho-jdt:
	Fatal error compiling:
	Unrecognized option : --add-modules=java.xml.bind -> [Help 1]
```

(Last checked: 9.0.4/10.0.1 and _org.eclipse.tycho : org.eclipse.jdt.core : 3.14.0.v20180528-0519_)
