
public class Foo {
    static {
        foo(Boolean.valueOf("true"));
        foo(new Boolean("false"));
    }
    private static void foo(Boolean b) {}
}
        