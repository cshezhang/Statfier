

public class Test {
  public Test() {} // violation, missing javadoc for constructor
  public void test() {} // violation, missing javadoc for method
  /**
   * Some description here.
   */
  public void test2() {} // OK

  @Override
  public String toString() { // OK
    return "Some string";
  }

  private void test1() {} // OK
  protected void test2() {} // OK
  void test3() {} // OK
}
        