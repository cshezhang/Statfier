import net.sourceforge.pmd.lang.java.rule.design.AvoidThrowingRawExceptionTypesTest.Error;
import net.sourceforge.pmd.lang.java.rule.design.AvoidThrowingRawExceptionTypesTest.Exception;
import net.sourceforge.pmd.lang.java.rule.design.AvoidThrowingRawExceptionTypesTest.RuntimeException;
import net.sourceforge.pmd.lang.java.rule.design.AvoidThrowingRawExceptionTypesTest.Throwable;

public class PmdTest {

  public void m() {
    throw new Throwable();
    throw new Error();
    throw new RuntimeException();
    throw new Exception();
  }
}

