

public class Test {
  private void test(int a) {
    switch (a) {
      case 1: someMethod();
      default: // OK, as there is no block
    }
    switch (a) {
      case 1: someMethod();
      default: {} // violation
    }
  }
}
        