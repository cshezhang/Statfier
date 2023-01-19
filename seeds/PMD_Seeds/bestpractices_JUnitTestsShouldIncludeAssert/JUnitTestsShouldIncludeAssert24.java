import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FooTest {

  @SuppressWarnings("deprecation")
  private static Foo foo = new Foo();

  @Test
  public void testFoo() {
    assertEquals("doesn't matter", "doesn't matter");
  }
}

