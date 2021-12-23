
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Foo {
    private static final Logger LOGGER = LogManager.getLogger(Foo.class);

    public void call() {
        final String message = "expected {} argument";
        LOGGER.error(message, 1);
    }
}
        