package iter0;

public class Foo {
    private final Object oSync = new Object();

    public void foo() {
        synchronized (oSync) {
            bar();
        }
    }
}
        