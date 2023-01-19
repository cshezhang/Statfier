public class Test {
  private int a; // violation, missing javadoc for private member

  /** Some description here */
  private int b; // OK

  protected int c; // OK
  public int d; // OK
  /*package*/ int e; // violation, missing javadoc for package member
}

