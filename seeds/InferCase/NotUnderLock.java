public class NotUnderLock {
  SettableFuture future = null;

  public void callFutureSetOk() {
    future.set(null);
  }

  public synchronized void firstAcquisitionBad() {
    callFutureSetOk();
  }

  public void secondAcquisitionOk(Object o) {
    synchronized (o) {
      firstAcquisitionBad();
    }
  }
}