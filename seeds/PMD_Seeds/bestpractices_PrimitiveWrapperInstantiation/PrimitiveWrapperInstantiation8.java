
public class Foo {
    public void go() {
        foo(Boolean.TRUE);
    }
    static {
        foo(Boolean.TRUE);
    }
    private static void foo(Boolean b) {}
}
        