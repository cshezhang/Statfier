
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

public class InvalidLogMessageFormatTest {
    private static final Logger logger = LoggerFactory.getLogger("MyLogger");
    private static final Marker marker = BasicMarkerFactory.getMarker("MARKER");

    public static void main(String[] args) {
        logger.warn(marker, "foo {}", "flibble", "moo", "blah", "blah"); // wrong number of arguments
        logger.warn(marker, "foo"); // correct: marker and no arguments
        logger.warn(marker, "foo", new Exception()); // correct: marker and one exception parameter
        logger.warn(marker, "foo {}", "bar"); // correct: marker and one argument

        final var otherMarker = MarkerFactory.getMarker("OTHER_MARKER");
        logger.warn(otherMarker, "foo"); // gets flagged as we can't statically determine the type of the "otherMarker" variable
    }
}
        