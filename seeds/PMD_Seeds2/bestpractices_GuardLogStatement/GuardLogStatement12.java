
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuardDebugFalsePositive {
    private static final Logger LOGGER = LoggerFactory.getLogger("GuardDebugFalsePositive");
    public void test() {
        String tempSelector = "a";
        LOGGER.debug("MessageSelector={}" , tempSelector);
    }
}
        