
public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            Throwable t = se.getCause();
        }
    }
}
        