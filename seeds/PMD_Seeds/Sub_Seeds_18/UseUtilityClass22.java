
import java.util.ArrayList;

public class FooLocal extends ThreadLocal<Integer> {
    public static FooLocal get() {
        return new FooLocal();
    }
}
        