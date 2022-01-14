
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

class MyJunit5Test {
  @Test
  void test() { /* ... */ }

  @Nested
  class InnerClassTest {
    @Test
    void test() { /* ... */ }
  }

  static class StaticNestedClassTest {
    @Test
    void test() { /* ... */ }
  }
}
