
public class MyClass {
  ...
  protected void finalize() {
    releaseSomeResources();    // Noncompliant
  }
  ...
}
