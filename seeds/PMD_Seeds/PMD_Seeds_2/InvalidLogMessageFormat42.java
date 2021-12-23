
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvalidLogMessageFormatTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidLogMessageFormatTest.class);

    private InvalidLogMessageFormatTest() {
    }

    public static void main(String[] args) {
        foo(arg -> LOGGER.error("Foo", arg)); // missing violation: extra argument, that is not a exception
        // explicit cast helps type resolution
        foo((String arg) -> LOGGER.error("Foo", arg)); // violation: extra argument, that is not a exception
        foo((String arg) -> LOGGER.error("Foo {}", arg)); // no violation: correct number of arguments
        foo(arg -> LOGGER.error("Foo {}", arg)); // no violation: correct number of arguments
        foo(arg -> LOGGER.error("Foo {} {}", arg)); // violation: missing argument
    }

    private static void foo(Consumer<String> consumer) {
        consumer.accept("bar");
    }
}
        