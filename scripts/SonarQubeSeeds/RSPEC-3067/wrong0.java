
public class MyClass {
  public void doSomethingSynchronized(){
    synchronized (this.getClass()) {  // Noncompliant
      // ...
    }
  }
