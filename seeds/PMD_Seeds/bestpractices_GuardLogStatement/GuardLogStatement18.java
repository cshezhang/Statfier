
import java.util.logging.Logger;
import java.util.logging.Level;

public class Foo {
    Logger LOGGER;

    public void foo() {
        if (LOGGER.isLoggable(Level.INFO)) { // should have been level FINE
            LOGGER.log(Level.FINE, "This is a severe message" + this + " and concat");
        }
    }
}
        