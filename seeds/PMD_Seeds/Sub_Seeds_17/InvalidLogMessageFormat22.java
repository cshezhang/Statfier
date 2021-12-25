
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Foo {
    private static final Logger LOGGER = LogManager.getLogger(Foo.class);

    public void call() {
        LOGGER.error("params {} and {}", "arg1", "arg2", new IllegalStateException("Extra arg"));
    }
}
        