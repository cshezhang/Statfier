import junit.framework.TestCase;

public class Foo extends TestCase {
  public void test1() {
    assertThat(0, is(not(1)));
  }
}

