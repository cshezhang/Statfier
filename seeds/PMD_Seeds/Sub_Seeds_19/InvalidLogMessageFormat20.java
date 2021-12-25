
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Foo {
    private static final Logger LOGGER = LogManager.getLogger(Foo.class);
    private final String MESSAGE = "too many args {}";

    public void call() {
        LOGGER.error(MESSAGE, "arg1", "arg2");
    }
}
         