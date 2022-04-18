

public class Test {

  public int a;
  protected int b;
  public int c;            // OK, access modifiers not considered while validating

  Test() {
    this.a = 0;
  }

  public void foo() {
    // This method does nothing
  }

  Test(int a) {            // violation, constructor definition in wrong order
    this.a = a;
  }

  private String name;     // violation, instance variable declaration in wrong order
}
        