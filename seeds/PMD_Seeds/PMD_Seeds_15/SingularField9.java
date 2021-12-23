
public class Foo {
    private Integer x = null;
    private Integer getFoo() {
        if (x == null) {
            x = new Integer(1);
        }
        return x;
    }
}
        