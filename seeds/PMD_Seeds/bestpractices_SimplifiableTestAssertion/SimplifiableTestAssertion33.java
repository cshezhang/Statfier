
import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

public class MyTestCase extends TestCase {

  public void testMyCaseWithAssertEqualsOnBoolean() {
    Object myVar = true;
    assertEquals(true, myVar);
    assertNotEquals(true, myVar);
  }
}

