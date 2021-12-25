
import org.slf4j.Logger;

public class LoggerHelper {
    public LoggerHelper(String loggerName) {
        Logger logger = LoggerFactory.getLogger(loggerName);
        logger.info(message);
    }
}
        