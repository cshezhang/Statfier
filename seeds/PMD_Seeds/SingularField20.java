package iter0;

public class Foo {
    private boolean bar = false;

    public void foo() {
        if (!bar) {
            onlyOnce();
            bar = true;
        }
    }
}
        