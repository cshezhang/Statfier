
package xxx;
public class Foo {
    public void bar() {
        String x;
        x = "foo";
        x += "bar"; // note, this won't be detected anymore due to #1736
        x += "baz";
    }
}
        