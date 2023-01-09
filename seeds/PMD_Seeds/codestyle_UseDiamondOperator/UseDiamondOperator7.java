
import java.lang.ref.WeakReference;

public class Foo {
    private WeakReference<Class<?>> typeReference;
    public void foo() {
        // this should be positive in Java 8, negative in Java 7
        // in java 7: no violation, in java 8 violation
        typeReference = new WeakReference<Class<?>>(String.class);
        // this is because in java 7, new WeakReference<>(String.class) types as WeakReference<Class<String>>
        // which is incompatible with WeakReference<Class<?>>, whereas Java 8's type inference is better.

        // the following is the same:
        // in java 7: no violation, in java 8 violation
        Class<?> type = null;
        typeReference = new WeakReference<Class<?>>(type);
    }
}
        