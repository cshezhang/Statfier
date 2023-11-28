import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Foo {

  Object a, b;

  @Test
  public void test1() {
    assertTrue(a.equals(b));
  }
}

