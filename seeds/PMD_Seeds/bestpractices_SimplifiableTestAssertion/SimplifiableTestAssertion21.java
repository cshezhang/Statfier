import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class Foo {
  @Test
  void testBar() {
    boolean bar;
    assertFalse(!bar);
  }
}

