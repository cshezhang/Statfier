
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;

class TestInvalidLogMessageFormat {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestInvalidLogMessageFormat.class);
    public void testPMD() {
        String message = String.format("Skipping file %s because no parser could be found", getName());
        LOGGER.info(message);
    }

    private String getName() { return "the-name"; }
}
        