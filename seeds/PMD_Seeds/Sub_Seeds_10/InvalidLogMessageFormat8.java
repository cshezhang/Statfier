
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidSl4jExceptionBug1541 {
    private static final Logger log = LoggerFactory.getLogger(InvalidSl4jExceptionBug1541.class);

    public static void main(String[] args) {
        try {
            // ...
        } catch (Exception e) {
            log.error("Arg1 = {}. Exception: {}", "arg1Value", e);
        }
    }
}
        