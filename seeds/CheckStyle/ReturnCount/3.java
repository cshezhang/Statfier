

public class MyClass {
  public int sign(int x) {
    if (x < 0)
      return -1;
    if (x == 0)
      return 1;
    return 0;
  } // OK

  public int badSign(int x) {
    if (x < -2)
      return -2;
    if (x == 0)
      return 0;
    if (x > 2)
      return 2;
    return 1;
  } // violation, more than three return statements per method
}
        