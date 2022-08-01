
import java.util.logging.Logger;
import java.util.logging.Level;

public class Foo {
    Logger LOGGER;

    public void foo() {
        LOGGER.severe("This is a severe message" + this + " and concat");
    }
}
        