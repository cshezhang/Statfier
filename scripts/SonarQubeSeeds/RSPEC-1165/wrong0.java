
public class MyException extends Exception {

  private int status;                               // Noncompliant

  public MyException(String message) {
    super(message);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
