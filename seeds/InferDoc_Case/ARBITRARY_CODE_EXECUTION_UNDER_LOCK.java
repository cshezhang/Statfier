import com.google.common.util.concurrent.SettableFuture;

public class ARBITRARY_CODE_EXECUTION_UNDER_LOCK {
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