
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class Test {
    private static final Log __log = LogFactory.getLog(Test.class);
    public void test() {

        // bad:
        __log.debug("log something" + this + " and " + "concat strings");

        // bad:
        __log.debug("log something" + this + " and " + "concat strings", e);

        // good:
        if (__log.isDebugEnabled()) {
        __log.debug("bla" + this, e);
        }
    }
}
        