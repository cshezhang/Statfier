
import java.util.logging.Logger;

public class Foo {

    private void foo(Logger logger) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("debug message: " + this);
        }
    }
}
        