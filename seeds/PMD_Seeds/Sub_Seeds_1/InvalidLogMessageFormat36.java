
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public enum LoggerHelper {
    INSTANCE;

    private final Logger log = LogManager.getLogger(LoggerHelper.class);

    public void sendMessage(String message) {
        log.info(message);
    }

    public static void main(String[] args) {
        LoggerHelper.INSTANCE.sendMessage("A message");
    }
}
        