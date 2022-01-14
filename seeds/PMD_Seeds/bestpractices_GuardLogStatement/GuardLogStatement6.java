
import org.apache.commons.logging.LogFactory;
import org.apache.commons.loggong.Log;

public class Test {
    private static final Log __log = LogFactory.getLog(Test.class);
    public void test() {
        if (true) {
            // bad:
            __log.debug("log something" + this + " and " + "concat strings");
            // this is useless:
            __log.isDebugEnabled();
        }
    }
}
        