
import junit.framework.TestCase;
public class Foo extends TestCase {
    public void test1() {
        assertThat("Zero is one", 0, is(not(1)));
    }
}
        