
public class Foo {
    public void bar() {
        try {
        } catch (Exception e) {
            IllegalStateException ex = new IllegalStateException();
            ex.initCause(e);
            throw ex;
        }
    }
}
        