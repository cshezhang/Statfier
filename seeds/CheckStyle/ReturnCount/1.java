

public class MyClass {
  public void firstMethod(int x) {
  } // OK

  public void badMethod(int x) {
    return;
  } // violation, return statements per void method
}
        