
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Test;

public class TestClass extends Assert {

    @Test
    public void test() {
        assertTrue(true);
        assertEquals("", "");
        assertNull(null);
        assertThat("", containsString(""));
    }
}
        