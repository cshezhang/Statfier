
import org.apache.logging.log4j.Logger;

public class LoggerHelper {
    static {
        final String pattern = "log: {}";

        Logger logger = LogManager.getLogger(loggerName);
        logger.info(pattern, 1, 2);
    }
}
        