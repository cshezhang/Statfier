

import android.support.annotation.GuardedBy;

class GUARDEDBY_VIOLATION_NULLSAFE {
    @GuardedBy("this")
    String f;
    void foo(String s) {
        f = s; // unprotected access here
    }
}