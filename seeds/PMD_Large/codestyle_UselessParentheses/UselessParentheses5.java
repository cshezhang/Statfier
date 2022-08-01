
public class Foo {
    public int bar(int x) {
        // actually, the inner parentheses wouldn't be necessary,
        // as the "||" operator has the lowest priority, followed by "=="
        // and by "%" with the highest priority here...
        // But for readability, it is better to have the parentheses as in
        // this example.
        if ((x % 2 == 0) || (x % 2 == 1)) {
            return x;
        } else {
            // it's the same with this extra parentheses
            return (x %2 == 0) ? x : -x;
        }
        return -x;
    }
}
        