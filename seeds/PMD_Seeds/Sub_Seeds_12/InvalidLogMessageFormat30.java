
import org.apache.logging.log4j.Logger;

public class LoggerHelper {
    public LoggerHelper(String loggerName) {
        Logger logger = LogManager.getLogger(loggerName);
        logger.info(message);
    }
}
        