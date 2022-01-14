
public class MyException extends Exception {

  private final int status;

  public MyException(String message, int status) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

}
