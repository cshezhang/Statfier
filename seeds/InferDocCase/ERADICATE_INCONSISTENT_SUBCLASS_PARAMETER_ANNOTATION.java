import android.support.annotation.Nullable;

public class ERADICATE_INCONSISTENT_SUBCLASS_PARAMETER_ANNOTATION {

    static class A {
        int len(@Nullable String s) {
            if (s != null) {
                return s.length();
            } else {
                return 0;
            }
        }
    }

    static class B extends A {
        int len(String s) {  // @Nullable missing.
            return s.length();
        }
    }

    String s;

    int foo() {
        A a = new B();
        return a.len(s);
    }
}