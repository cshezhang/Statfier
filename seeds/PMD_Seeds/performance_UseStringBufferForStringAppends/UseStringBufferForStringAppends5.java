
public class Foo {
    static {
        String x;
        x = "foo";
        x += "bar"; // note, due to #1736 one concatenation is not enough
        x += "baz";
    }
}
        