package iter0;

public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            throw se.getCause();
        }
    }
}
        