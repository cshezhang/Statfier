
import java.lang.ref.WeakReference;

public class Foo {
    private WeakReference<Class<?>> typeReference;
    public void foo() {
        typeReference = new WeakReference<Class<?>>(String.class); // pos
        Class<?> type = null;
        typeReference = new WeakReference<Class<?>>(type); // pos
    }
}
        