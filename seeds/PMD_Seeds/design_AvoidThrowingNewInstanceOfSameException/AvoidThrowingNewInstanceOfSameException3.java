
public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            throw new SomeException("new exception message", se);
        }
    }
}
        