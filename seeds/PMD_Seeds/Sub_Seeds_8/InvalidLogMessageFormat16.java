
import org.slf4j.Logger;

public class LoggerHelper {
    final Logger logger = LoggerFactory.getLogger(loggerName);

    final List<String> list = someMethod(message -> {
        final String pattern = "log: {}";

        logger.info(pattern, 1, 2);
    });
}
        