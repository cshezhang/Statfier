
import java.util.ArrayList;
import java.util.List;

public class Bar {
    void foo() {
        Integer[] ints = new Integer(10);
        List l = new ArrayList(10);
        for (int i = 0; i < 100; i++) {
            l.add(String.valueOf(i));
        }
    }
}
        