

public class Test {
  public void func1() throws RuntimeException {} // ok
  public void func2() throws Exception {}  // ok
  public void func3() throws Error {}  // ok
  public void func4() throws Throwable {} // ok
  public void func5() throws NullPointerException {} // violation
  @Override
  public void toString() throws Error {} // ok
}
        