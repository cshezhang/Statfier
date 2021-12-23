
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Foo.class);

    public void call() {
        LOGGER.error("params {} and {}", "arg1", "arg2", new IllegalStateException("Extra arg"));
    }
}
        