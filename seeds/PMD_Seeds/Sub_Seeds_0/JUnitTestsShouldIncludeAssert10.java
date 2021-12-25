
package at.herold.anthilltest;
import junit.framework.TestCase;
public class TestJunitRuleExceptionTest extends TestCase {
    interface I1 {
        public void meth(); // this is ok
    }
    interface I2 {
        public void meth() throws Exception; // this causes PMDException
    }
}
        