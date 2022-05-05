

public class MyClass {
  public void firstMethod() {
  } // OK

  public void secondMethod() {
    return;
  } // OK

  public void badMethod(int x) {
    if (x == 0)
      return;
    return;
  } // violation, more than one return statements

  public int sign(int x) {
    if (x < 0)
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
        