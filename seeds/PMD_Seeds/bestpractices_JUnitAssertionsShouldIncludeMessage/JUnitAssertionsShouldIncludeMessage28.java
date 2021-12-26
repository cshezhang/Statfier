
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TestNgTestCase {
    @Test
    public void testAssert() {
        assertEquals("a", "b", "message");
        assertEquals(1, 2, "message");
        assertFalse(true, "message");
        assertTrue(false, "message");
        assertNotEquals(new Object(), new Object(), "message");
        assertNotNull(null, "message");
        assertNull(null, "message");
    }
}
        