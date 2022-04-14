



import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class NotThreadSafeExample {

  Integer f;

  public void tsBad() {
    /*Shouldn't report*/
    f = 24;
  }
}
