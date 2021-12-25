
import org.apache.log4j.Logger;

public class GuardLogTest {
    Logger LOG;
    public void foo() {
        if (LOG.isInfoEnabled()) {
            LOG.info("update: After spool map size: " + map.size());
        }
    }
}
        