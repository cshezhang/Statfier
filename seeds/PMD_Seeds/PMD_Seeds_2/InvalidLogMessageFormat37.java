
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public enum LoggerHelper {
    INSTANCE;

    private static final String pattern = "log: {}";

    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(LoggerHelper.class);

        logger.info(pattern, 1, 2);
    }
}
        