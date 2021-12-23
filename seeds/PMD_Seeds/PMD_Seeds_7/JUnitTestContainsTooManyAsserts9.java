
import org.testng.annotations.Test;

public class MyTestCase {
    @Test
    public void testRegular() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }
}
        