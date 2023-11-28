import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {
  private static final Log __log = LogFactory.getLog(Test.class);

  public void test() {
    // okay:
    __log.debug("log something");

    // okay:
    __log.debug("log something with exception", e);

    // good:
    if (__log.isDebugEnabled()) {
      __log.debug("bla: " + this, e);
    }
  }
}

