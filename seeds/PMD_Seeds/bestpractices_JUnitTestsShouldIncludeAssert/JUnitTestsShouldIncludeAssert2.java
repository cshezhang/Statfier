
import junit.framework.TestCase;
public class FooTest extends TestCase {
    public void setUp() {
    }
    public void test1() {
        assertTrue("foo", "foo".equals("foo"));
    }
    public void test2() {
        assertEquals("foo", "foo");
    }
}
        