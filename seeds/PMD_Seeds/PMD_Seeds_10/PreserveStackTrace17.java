
public class Foo {
    public void bar1() {
        try {
        } catch (Exception notUsed) {
            RuntimeException ex = new RuntimeException();
            throw ex;
        }
    }

    public void bar2() {
        try {
        } catch (Exception e) {
            IllegalStateException ex = new IllegalStateException();
            ex.initCause(e);
            throw ex;
        }
    }
}
        