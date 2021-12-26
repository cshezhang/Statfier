
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class FooTest {

    @Test
    void testFoo() {
        var softly = new SoftAssertions();
        softly.assertThat("doesn't matter").isEqualTo("doesn't matter");
        softly.assertAll();
    }

    @Test
    void testBar() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat("doesn't matter").isEqualTo("doesn't matter");
        softly.assertAll();
    }
}
        