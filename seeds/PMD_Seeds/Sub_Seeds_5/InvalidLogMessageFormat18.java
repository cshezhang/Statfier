
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LoggerHelper {
    INSTANCE;

    private static final String pattern = "log: {}";

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(LoggerHelper.class);

        logger.info(pattern, 1, 2);
    }
}
        