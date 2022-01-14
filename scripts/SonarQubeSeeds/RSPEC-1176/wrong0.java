
/**
  * This is a Javadoc comment
  */
public class MyClass<T> implements Runnable {    // Noncompliant - missing '@param <T>'

  public static final DEFAULT_STATUS = 0;    // Compliant - static constant
  private int status;                           // Compliant - not public

  public String message;                  // Noncompliant

  public MyClass() {                         // Noncompliant - missing documentation
    this.status = DEFAULT_STATUS;
  }

  public void setStatus(int status) {  // Compliant - setter
    this.status = status;
  }

  @Override
  public void run() {                          // Compliant - has @Override annotation
  }

  protected void doSomething() {    // Compliant - not public
  }

  public void doSomething2(int value) {  // Noncompliant
  }

  public int doSomething3(int value) {  // Noncompliant
    return value;
  }
}
