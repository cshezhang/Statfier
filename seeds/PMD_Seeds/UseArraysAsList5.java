package iter0;

public class Test {
    public void foo(Integer[] ints) {
        // could just use Arrays.asList(ints)
        List l = new ArrayList(10);
        for (int i = 0; i < 100; i++) {
        l.add(ints[i]);
        }
        for (int i = 0; i < 100; i++) {
        l.add(a[i].toString()); // won't trigger the rule
        }
    }
}
        