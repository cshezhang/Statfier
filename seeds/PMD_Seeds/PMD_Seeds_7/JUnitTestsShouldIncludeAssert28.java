
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Test;

public class FooTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    void testFoo() {
        softly.assertThat("doesn't matter").isEqualTo("doesn't matter");
    }
}
        