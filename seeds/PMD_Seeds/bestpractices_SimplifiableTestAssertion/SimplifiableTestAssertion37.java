import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Foo {

  @Test
  public void test() {
    assertTrue(!Thread.currentThread().getName().equals(event.content));
    assertTrue(Thread.currentThread().getName().equals(event.content));
  }
}

