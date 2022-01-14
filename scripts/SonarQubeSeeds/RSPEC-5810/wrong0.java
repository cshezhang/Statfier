
import org.junit.jupiter.api.Test;

class MyClassTest {
  @Test
  private void test1() { // Noncompliant - ignored by JUnit5
    // ...
  }
  @Test
  static void test2() { // Noncompliant - ignored by JUnit5
    // ...
  }
  @Test
  boolean test3() { // Noncompliant - ignored by JUnit5
    // ...
  }
  @Nested
  private class MyNestedClass { // Noncompliant - ignored by JUnit5
    @Test
    void test() {
      // ...
    }
  }
}
