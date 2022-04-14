import android.support.annotation.GuardedBy;

public class GUARDEDBY_VIOLATION {
  @GuardedBy("this")
  String f;

  void foo(String s) {
    f = s; // unprotected access here
  }
}