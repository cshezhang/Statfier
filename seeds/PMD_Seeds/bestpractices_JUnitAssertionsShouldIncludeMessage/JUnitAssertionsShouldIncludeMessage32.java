import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpleTest {
  @Test
  public void simpleMethod() {
    assertEquals(0, Integer.compare(0, 0), "Not equals 0 != 1");
  }
}

