

import android.content.Context;
import android.view.View;
import java.util.concurrent.Executor;

class MyView extends View {
  MyView(Context c) {
    super(c);
  }

  Object monitorA, monitorB;
  @ForNonUiThread private final Executor mNonUiThreadExecutor = null;

  void scheduleOnBGThread() {
    mNonUiThreadExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            synchronized (monitorA) {
              synchronized (monitorB) {
              }
            }
          }
        });
  }

  void scheduleOnUIThread() {
    post(
        new Runnable() {
          @Override
          public void run() {
            synchronized (monitorB) {
              synchronized (monitorA) {
              }
            }
          }
        });
  }
}
