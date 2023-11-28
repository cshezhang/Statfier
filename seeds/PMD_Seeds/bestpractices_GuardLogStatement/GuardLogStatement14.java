import java.util.logging.Level;
import java.util.logging.Logger;

public class Foo {
  Logger LOGGER;

  public void foo() {
    LOGGER.log(Level.FINE, "This is a severe message" + this + " and concat"); // violation
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.log(Level.FINE, "This is a severe message" + this + " and concat"); // no violation
    }
  }
}

