package iter0;

public class Foo {
    void bar() {
        Exception e = new RuntimeException();
        e = new NullPointerException();
        throw e;
    }
}
        