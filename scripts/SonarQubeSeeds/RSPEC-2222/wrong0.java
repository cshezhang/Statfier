
public class MyClass {
  public void doSomething() {
    Lock lock = new Lock();
    lock.lock(); // Noncompliant
    if (isInitialized()) {
      // ...
      lock.unlock();
    }
  }
}
