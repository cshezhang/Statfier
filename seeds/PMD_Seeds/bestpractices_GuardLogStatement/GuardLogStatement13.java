import java.util.logging.Logger;

public class Foo {
  Logger LOGGER;

  public void foo() {
    LOGGER.severe("This is a severe message" + this + " and concat");
  }
}

