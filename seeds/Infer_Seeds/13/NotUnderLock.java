

import com.google.common.util.concurrent.SettableFuture;

public class NotUnderLock {
  SettableFuture future = null;

  private void callFutureSetOk() {
    future.set(null);
  }

  private synchronized void firstAcquisitionBad() {
    callFutureSetOk();
  }

  private void secondAcquisitionOk(Object o) {
    synchronized (o) {
      firstAcquisitionBad();
    }
  }
}
