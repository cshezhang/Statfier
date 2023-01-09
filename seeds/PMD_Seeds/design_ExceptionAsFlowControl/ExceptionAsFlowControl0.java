
public class Foo {
    void bar() {
        try {
            try {
            } catch (Exception e) {
                throw new WrapperException(e);
                // this is essentially a GOTO to the WrapperException catch block
            }
        } catch (WrapperException e) {
            // do some more stuff
        }
    }
}
        