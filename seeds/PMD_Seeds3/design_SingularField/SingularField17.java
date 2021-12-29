
public class Foo {
    private static Foo FOO = new Foo();
    private int x;
    static {
        FOO.x = 5;
    }
    int bar(int y) {
        x = y + 5;
        return x;
    }
}
        