
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class InvalidSl4jExceptionBug1541 {
    private static final Logger log = LogManager.getLogger(InvalidSl4jExceptionBug1541.class);

    public static void main(String[] args) {
        try {
            // ...
        } catch (Exception e) {
            log.error("Arg1 = {}. Exception: {}", "arg1Value", e);
        }
    }
}
        