
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MyTests {
    @Test
    public void testRegular() { }

    @RepeatedTest(2)
    public void testRepeated() { }

    @TestFactory
    public void testFactory() { }

    @TestTemplate
    public void testTemplate() { }

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "World"})
    public void testParameterized(final String value) { }
}
        