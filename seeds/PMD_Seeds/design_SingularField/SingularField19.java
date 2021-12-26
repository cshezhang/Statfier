
public class Foo {
    private final Object sync = new Object();
    private final Object noSync;

    public void foo() {
        synchronized (sync) {
            noSync = new String("test");
            call(noSync);
        }
    }
}
        