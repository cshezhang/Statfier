
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Foo.class);
    private final String MESSAGE = "too many args {}";

    public void call() {
        LOGGER.error(MESSAGE, "arg1", "arg2");
    }
}
        