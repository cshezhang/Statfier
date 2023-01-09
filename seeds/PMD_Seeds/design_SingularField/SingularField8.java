
public class Foo {
    private int x;
    public Foo(int y) {
        x = y;
    }
    int bar(int y) {
        x = y + 5;
        return x;
    }
}
        