package wtf.java9.symbolic_references;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

public class TestClassGenerator {
  private static final Method defineClass;       
  static {
    try {
      defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchMethodException 'defineClass' on ClassLoader.\n"
          + "This is most likely an environment issue.", e);
    }
    defineClass.setAccessible(true);
  }

  public Class<? extends TestClass> generateClass() {
    return defineClass(getClass().getClassLoader());
    
  }

  byte[] generateBytecode() {
    ClassNode cv = new ClassNode();
    
    cv.visit(V1_8, 
             ACC_PUBLIC | ACC_SYNTHETIC | ACC_SUPER, 
             "TestClassImpl", 
             null, 
             "java/lang/Object", 
             new String[] { "wtf/java9/symbolic_references/TestClass" });
    
    generateConstructor(cv);
    generateGeneratedMethod(cv);
    
    cv.visitEnd();
    
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    cv.accept(writer);
    return writer.toByteArray();    
  }
  
  void generateConstructor(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC, 
        "<init>", 
        "()V",
        "", 
        null);

    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    mv.visitInsn(RETURN);
    mv.visitEnd();        
  }
  
  void generateGeneratedMethod(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC, 
        "generatedMethod", 
        "(Ljava/lang/Object;)Ljava/lang/String;",
        "", 
        null);

    mv.visitCode();
    mv.visitVarInsn(ALOAD, 1);
    
    // The following line initiates the failure. Java 9 is 
    // more strict here and requires an internal name
    // rather than a descriptor. So change to "java/lang/String".
    mv.visitTypeInsn(CHECKCAST, "Ljava/lang/String;"); 
    
    mv.visitInsn(ARETURN);
    mv.visitEnd();    
  }

  @SuppressWarnings("unchecked")
  Class<? extends TestClass> defineClass(ClassLoader loader) {
    byte[] code = generateBytecode();
    try {
      return (Class<? extends TestClass>)defineClass.invoke(loader, "TestClassImpl", code, 0, code.length);      
    } catch (InvocationTargetException e) {
      System.err.println("InvocationTargetException: in defineClass: " + e.getMessage());      
      throw new RuntimeException("Unrecoverable Error", e);
    } catch (IllegalAccessException e) {
      System.err.println("IllegalAccessException: in defineClass: " + e.getMessage());      
      throw new RuntimeException("Unrecoverable Error", e);
    }
  }

}
