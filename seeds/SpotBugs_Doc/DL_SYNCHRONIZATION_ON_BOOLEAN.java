public class DL_SYNCHRONIZATION_ON_BOOLEAN {
  private static Boolean inited = Boolean.FALSE;

  public void foo() {
    synchronized (inited) {
      if (!inited) {
        init();
        inited = Boolean.TRUE;
      }
    }
  }
}

