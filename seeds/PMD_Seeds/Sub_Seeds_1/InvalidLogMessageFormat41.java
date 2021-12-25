
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvalidLogMessageFormatTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidLogMessageFormatTest.class);

    private InvalidLogMessageFormatTest() {
    }

    public static void main(String[] args) {
        foo(exception -> LOGGER.error("Foo", exception));
    }

    private static void foo(Consumer<Throwable> consumer) {
        consumer.accept(new IllegalArgumentException());
    }
}
        