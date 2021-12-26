
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Test {
    public void test(String mymarker) {
        final Logger logger = LogManager.getLogger(Test.class);
        final Marker m = MarkerManager.getMarker(mymarker);
        logger.isDebugEnabled(m); // must be within an if
        logger.debug(m, message + "");
    }
}
        