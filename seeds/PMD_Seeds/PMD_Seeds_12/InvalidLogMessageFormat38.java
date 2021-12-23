
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class InvalidLogMessageFormatTest {
    private static final Logger logger = LogManager.getLogger("MyLogger");

    public static void main(String[] args) {
        logger.warn("foo {}", "flibble", "moo", "blah", "blah"); // PMD flags this
        logger.warn("foo", "flibble", "moo", "blah", "blah"); // PMD doesn't flag this
    }
}
        