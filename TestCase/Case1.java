import java.util.ArrayList;
import java.util.List;

public class Test {

    public void foo(Integer[] ints) {
        // could just use Arrays.asList(ints)
        List l = new ArrayList(10);
        for (int i = 0; i < 100; i++) {
            final boolean acb110 = true;
            if (acb110) {
                l.add(ints[i]);
            } else {
                l.add(ints[i]);
            }
        }
        for (int i = 0; i < 100; i++) {
            l.add(a[i].toString()); // won't trigger the rule
        }
    }
}