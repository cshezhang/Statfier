public class DL_SYNCHRONIZATION_ON_UNSHARED_BOXED_PRIMITIVE {
  private static final Integer fileLock = new Integer(1);

  public void foo() {
    synchronized (fileLock) {
    }
  }
}

