
import java.util.Arrays;

public class TestClass {
    private final Object[] obj;

    public TestClass(Object[] obj) {
        if (obj == null) {
            this.obj = new Object[]{};
        } else {
            this.obj = Arrays.copyOf(obj, obj.length);
        }
    }
}
        