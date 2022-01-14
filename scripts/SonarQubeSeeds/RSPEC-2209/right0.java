
public class A {
  public static int counter = 0;
}

public class B {
  private A first = new A();
  private A second = new A();

  public void runUpTheCount() {
    A.counter ++;  // Compliant
    A.counter ++;  // Compliant
  }
}
