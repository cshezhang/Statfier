
public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            throw new SomeException(se.getMessage());
        }
    }
}
        