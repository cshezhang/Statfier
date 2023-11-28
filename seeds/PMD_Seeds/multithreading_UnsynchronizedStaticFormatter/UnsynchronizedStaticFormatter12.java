import java.text.SimpleDateFormat;

public class Foo {
  private static final SimpleDateFormat sdf = new SimpleDateFormat();

  static synchronized void bar() { // not a synchronized blocks
    sdf.format();
  }
}

