
public class Foo {
    private static int x = 0;
    private final int y;

    public Foo() {
        y = x;
        x++;
    }
}
        