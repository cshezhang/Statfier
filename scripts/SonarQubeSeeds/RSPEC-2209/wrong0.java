
public class A {
  public static int counter = 0;
}

public class B {
  private A first = new A();
  private A second = new A();

  public void runUpTheCount() {
    first.counter ++;  // Noncompliant
    second.counter ++;  // Noncompliant. A.counter is now 2, which is perhaps contrary to expectations
  }
}
