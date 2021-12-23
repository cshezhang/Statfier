
import junit.framework.TestCase;
public class Foo extends TestCase {
    public void test1() {
        assertNotSame("1 == 2", 1, 2);
    }
}
        