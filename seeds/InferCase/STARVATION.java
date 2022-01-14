  import android.annotation.SuppressLint;

  @SuppressLint("STARVATION")
  public void m() {
  ...
  }

  import com.facebook.infer.annotation.NonBlocking;

  @NonBlocking
  public void m() {
  ...
  }