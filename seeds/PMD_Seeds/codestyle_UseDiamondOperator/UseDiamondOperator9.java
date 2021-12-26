
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.lang.ref.WeakReference;

public class UseDiamondOperatorFalseNegative {
    List<Map<String,Object>> l = new ArrayList<Map<String,Object>>(); // FN
    WeakReference<Class<String>> typeReference = new WeakReference<Class<String>>(String.class); // FN
    WeakReference<Class<?>> typeReference2 = new WeakReference<Class<?>>(String.class); // FP

    public void test() {
        final List<String> l2;
        l2 = true ? new ArrayList<String>() : new ArrayList<String>(); // FN twice for java8+, but for java7, this is ok!
    }

    static {
        l = new ArrayList<Map<String,Object>>(); // FN
    }
}
        