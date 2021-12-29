
public class Foo {
    void bar() {
        Exception e = new NullPointerException();
        e = new RuntimeException();
        throw e;
    }
}
        