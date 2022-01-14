
/**
  * This is a Javadoc comment
  * @param <T> the parameter of the class
  */
public class MyClass<T> implements Runnable {

  public static final DEFAULT_STATUS = 0;
  private int status;

  /**
    * This is a Javadoc comment
    */
  public String message;

  /**
   * Class comment...
   */
  public MyClass() {
    this.status = DEFAULT_STATUS;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public void run() {
  }

  protected void doSomething() {
  }

  /**
    * Will do something.
    * @param value the value to be used
    */
  public void doSomething(int value) {

  /**
    *  {@inheritDoc}
    */
  public int doSomething(int value) {
    return value;
  }
}
