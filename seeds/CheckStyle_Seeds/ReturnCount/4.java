

import java.util.function.Predicate;

public class Test {
  public Test() {
  } // OK

  public Test(int i) {
    return; // violation, max allowed for constructors is 0
  }

  final Predicate<Integer> p = i -> {
    if (i > 5) {
      return true;
    }
    return false;
  }; // violation, max allowed for lambdas is 1

  final Predicate<Integer> q = i -> {
    return i > 5;
  }; // OK

  public int sign(int x) {
    if (x > 0)
      return -1;
    return 0;
  } // OK

  public int badSign(int x) {
    if (x < 0)
      return -1;
    if (x == 0)
      return 1;
    return 0;
  } // violation, more than two return statements in methods
}
        