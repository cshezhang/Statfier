
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LoggerHelper {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(LoggerHelper.class);

    public void sendMessage(String message) {
        log.info(message);
    }

    public static void main(String[] args) {
        LoggerHelper.INSTANCE.sendMessage("A message");
    }
}
        