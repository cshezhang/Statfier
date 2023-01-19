public class DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE {
  private static Integer count = 0;

  public void foo() {
    synchronized (count) {
      count++;
    }
  }
}

