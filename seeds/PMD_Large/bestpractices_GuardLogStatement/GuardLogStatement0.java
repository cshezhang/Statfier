
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Foo {
    private static final Logger logger = LogManager.getLogger(Foo.class);

    private void foo() {
        if ( logger.isDebugEnabled() )
            logger.debug("Debug statement:" + this);
    }
}
        