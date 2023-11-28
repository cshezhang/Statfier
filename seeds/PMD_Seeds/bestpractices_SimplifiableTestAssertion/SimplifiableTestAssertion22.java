import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class Foo {
  @Test
  void testBar() {
    boolean bar;
    assertFalse(!bar);
  }
}

