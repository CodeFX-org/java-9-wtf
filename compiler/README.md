# Compiler

## Type Inference

It looks like the Java 9 compiler's type inference works differently in some cases, which can lead to new compile errors.
All files in this project should compile fine with Java 8 (tested with u121) but fail with Java 9 (tried with Jigsaw EA b163).
Look for comments `// fail` to see what doesn't work and `// pass` for fixes that make it work in Java 9.

* [casts like `(int) Comparable<T>`](src/wtf/java9/compiler/CastWildcardParam.java)
* [shenanigans with generic arrays](src/wtf/java9/compiler/GenericArray.java)
