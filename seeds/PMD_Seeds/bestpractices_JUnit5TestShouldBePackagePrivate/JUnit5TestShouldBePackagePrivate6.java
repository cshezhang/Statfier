
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

protected class MyTests {
    @Test
    public void testRegular() { }

    @RepeatedTest(2)
    protected void testRepeated() { }

    @TestFactory
    private void testFactory() { }

    @TestTemplate
    public void testTemplate() { }

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "World"})
    protected void testParameterized(final String value) { }
}
        