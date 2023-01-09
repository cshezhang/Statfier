
public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            try {
            } catch (OtherException oe) {
                throw new SomeException(se);
            }
        }
    }
}
        