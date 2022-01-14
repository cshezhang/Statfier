
public class MyClass {
  public void doSomething() {
    Lock lock = new Lock();
    if (isInitialized()) {
      lock.lock();
      // ...
      lock.unlock();
    }
  }
}
