
import org.apache.log4j.Logger;

public class GuardLogTest {
    Logger LOG;
    public void foo() {
        if (!(exception instanceof IllegalArgumentException) && LOGGER.isErrorEnabled()) {
            LOGGER.error("Some error " + someObject, exception);
        }
    }
}
        