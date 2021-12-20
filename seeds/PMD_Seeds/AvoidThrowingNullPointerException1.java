package iter0;

public class Foo {
    void bar() {
        Exception e = new NullPointerException("Test message");
        String msg = e.getMessage();
    }
}
        