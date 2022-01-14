
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTest {
    @Test
    public void myTest() {
        Bean bean = new Bean();
        bean.doSomething("foo");
    }
}

class Bean {
    public void doSomething(String name) {
        assertEquals("foo", name);
    }
}
        