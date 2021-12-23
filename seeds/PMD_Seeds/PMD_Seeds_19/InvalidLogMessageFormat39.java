
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidLogMessageFormatTest {
    private static final Logger logger = LoggerFactory.getLogger("MyLogger");

    public static void main(String[] args) {
        logger.warn("foo {}", "flibble", "moo", "blah", "blah"); // PMD flags this
        logger.warn("foo", "flibble", "moo", "blah", "blah"); // PMD doesn't flag this
    }
}
        