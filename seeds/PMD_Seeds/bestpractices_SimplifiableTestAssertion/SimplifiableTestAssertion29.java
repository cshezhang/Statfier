import org.junit.Assert;
import org.junit.Test;

public class Foo {
  Object a, b;

  @Test
  public void test1() {
    Assert.assertTrue(a.equals(b));
  }
}

