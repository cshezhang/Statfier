
public class MyClass {

  Thread thread = null;

  public MyClass(Runnable runnable) {
    thread = new Thread(runnable);
    thread.start(); // Noncompliant
  }
}
