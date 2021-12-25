
import org.apache.logging.log4j.Logger;

public class LoggerHelper {
    final Logger logger = LogManager.getLogger(loggerName);

    final List<String> list = someMethod(message -> logger.info(message));
}
        