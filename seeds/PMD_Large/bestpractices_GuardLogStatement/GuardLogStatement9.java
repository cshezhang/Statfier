
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Test {
    public void test(String mymarker) {
        final Logger logger = LogManager.getLogger(Test.class);
        final Marker m = MarkerManager.getMarker(mymarker);
        if (logger.isTraceEnabled(m)) { // should have been isDebugEnabled
          logger.debug(m, message + "");
        }
        if (logger.isDebugEnabled(m)) { // should have been isTraceEnabled
          logger.trace(m, message + "");
        }
    }
}
        