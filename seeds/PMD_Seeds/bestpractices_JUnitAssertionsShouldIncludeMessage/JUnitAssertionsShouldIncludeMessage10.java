import junit.framework.TestCase;

public class Foo extends TestCase {
  public void testBar() {
    assertFalse("foo".equals("foo"));
  }
}

