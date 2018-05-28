# Symbolic References

This is related to the class loading tests, and really falls into the "Should Break" category,
as previous behaviour was not strictly to-spec.

## Stricter interpretation of Symbolic References in .class files

Java 9 appears to be more strict in its interpretation of the term "Symbolic Reference" in
class files. The spec says:

> For a nonarray class or an interface, the name is the fully qualified name of the class or interface.

(This is expected in "binary format", with forward-slashes instead of periods, for example
`java/lang/String`.)

In Java 8, you could get away with using a _Type Descriptor_ when really you should have
used a symbolic reference. A good example is CHECKCAST (which is used in this test). 
Under Java 8, the instruction could have referenced a constant pool entry with either
`java/lang/String` (to spec) or `Ljava/lang/String;` (a descriptor, and not strictly
to spec) and it would work fine. 

Under Java 9, such classes will not load. You'll instead receive a `ClassFormatError`:

```
[ERROR] loadClassWithDescriptorCheckcast  Time elapsed: 0.031 s  <<< ERROR!
java.lang.RuntimeException: Unrecoverable Error
        at wtf.java9.symbolic_references.CheckCastTest.loadClassWithDescriptorCheckcast(CheckCastTest.java:13)
Caused by: java.lang.reflect.InvocationTargetException
        at wtf.java9.symbolic_references.CheckCastTest.loadClassWithDescriptorCheckcast(CheckCastTest.java:13)
Caused by: java.lang.ClassFormatError: Illegal class name "Ljava/lang/String;" in class file TestClassImpl
        at wtf.java9.symbolic_references.CheckCastTest.loadClassWithDescriptorCheckcast(CheckCastTest.java:13)
```
