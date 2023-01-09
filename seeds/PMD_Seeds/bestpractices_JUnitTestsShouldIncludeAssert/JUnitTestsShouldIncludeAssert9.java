
import junit.framework.TestCase;
public class FooTest extends TestCase {
    public void testNPEThrown() {
        try {
            methodCall(null);
            fail("Expected NullPointerException to be thrown.");
        } catch (NullPointerException npe) {
            // Caught expected exception
        }
    }
}
        