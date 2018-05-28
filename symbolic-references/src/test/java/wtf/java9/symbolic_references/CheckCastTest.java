package wtf.java9.symbolic_references;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import wtf.java9.symbolic_references.TestClass;
import wtf.java9.symbolic_references.TestClassGenerator;

public class CheckCastTest {
  
  /* Works under Java < 9, fails with ClassFormatError under Java 9. */
  @Test
  public void loadClassWithDescriptorCheckcast() throws Exception {
    Class<? extends TestClass> clz = new TestClassGenerator().generateClass();    
    TestClass testClass = clz.newInstance();
    assertThat(testClass.generatedMethod("hello")).isEqualTo("hello");
  }
}
