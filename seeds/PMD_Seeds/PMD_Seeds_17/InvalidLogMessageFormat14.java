
import org.slf4j.Logger;

public class LoggerHelper {
    static {
        final String pattern = "log: {}";

        Logger logger = LoggerFactory.getLogger(loggerName);
        logger.info(pattern, 1, 2);
    }
}
        