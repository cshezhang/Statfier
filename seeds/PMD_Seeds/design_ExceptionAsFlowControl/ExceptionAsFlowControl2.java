
public class Foo {
    void bar() {
        try {
        } catch (IOException e) {
            if (foo!=null)
                throw new IOException(foo.getResponseMessage());
            else
                throw e;
        }
    }
}
        