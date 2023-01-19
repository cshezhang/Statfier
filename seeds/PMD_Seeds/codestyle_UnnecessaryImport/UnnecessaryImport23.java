import static java.lang.Math.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class FooTest {
  @Test
  public void testStuff() {
    assertEquals("a", "a");
    assertFalse(false);
    assertTrue(true);
    assertNull(null);
  }

  public static void main(String[] args) {
    System.out.println(PI);
  }
}

