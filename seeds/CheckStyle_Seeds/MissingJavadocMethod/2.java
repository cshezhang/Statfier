

public class Test {
  private void test1() {} // violation, the private method is missing javadoc
  /**
   * Some description here
   */
  private void test1() {} // OK
  protected void test2() {} // OK
}
        