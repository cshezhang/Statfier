
import junit.framework.TestCase;
public class Foo extends TestCase {
    public void test1() {
        assertArrayEquals("[1] != [1]", new int[] {1}, new int[] {1});
    }
}
        