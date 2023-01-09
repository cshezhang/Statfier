
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

public class Outer {
    private static Stream<Arguments> basenameKeyArguments() {
        return Stream.of(
                Arguments.of("simple", "simple"),
                Arguments.of("simple", "one/two/many/simple"),
                Arguments.of("simple", "//////an/////awful/key////simple")
        );
    }

    @ParameterizedTest
    @MethodSource("basenameKeyArguments")
    void basenameKeyTest(final String expected, final String testString) {
        assertEquals(expected, NetworkTable.basenameKey(testString));
    }

}
        