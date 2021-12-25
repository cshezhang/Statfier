
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MyTestCase {
    @Test
    public void testRegular() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }

    @RepeatedTest(2)
    public void testRepeated() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }

    @TestFactory
    public void testFactory() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }

    @TestTemplate
    public void testTemplate() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "World"})
    public void testParameterized(String value) {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }
}
        