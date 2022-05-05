

public class Test {
  public void test() {} // violation, method is missing javadoc
  @Override
  public void test1() {} // OK
  @Deprecated
  public void test2() {} // OK
  @SuppressWarnings
  public void test3() {} // violation, method is missing javadoc
  /**
   * Some description here.
   */
  @SuppressWarnings
  public void test4() {} // OK
}
        