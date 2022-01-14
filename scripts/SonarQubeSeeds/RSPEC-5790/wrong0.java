
import org.junit.jupiter.api.Test;

class MyJunit5Test {
  @Test
  void test() { /* ... */ }

  class InnerClassTest { // Noncompliant, missing @Nested annotation
    @Test
    void test() { /* ... */ }
  }

  @Nested
  static class StaticNestedClassTest { // Noncompliant, invalid usage of @Nested annotation
    @Test
    void test() { /* ... */ }
  }
}
