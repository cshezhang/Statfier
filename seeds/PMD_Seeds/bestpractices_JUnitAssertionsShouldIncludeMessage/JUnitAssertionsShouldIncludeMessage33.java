import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTest {
  @Test
  public void myTest() {
    Bean bean = new Bean();
    bean.doSomething("foo");
  }
}

class Bean {
  public void doSomething(String name) {
    assertEquals("foo", name);
  }
}

