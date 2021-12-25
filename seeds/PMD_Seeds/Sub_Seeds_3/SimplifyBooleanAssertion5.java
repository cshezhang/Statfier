
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class Foo {
    @Test
    void testBar() {
        boolean bar;
        assertFalse(!bar);
    }
}
        