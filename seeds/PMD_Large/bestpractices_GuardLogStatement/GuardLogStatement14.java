
import java.util.logging.Logger;
import java.util.logging.Level;

public class Foo {
    Logger LOGGER;

    public void foo() {
        LOGGER.log(Level.FINE, "This is a severe message" + this + " and concat"); // violation
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "This is a severe message" + this + " and concat"); // no violation
        }
    }
}
        