package iter0;

public class Foo {
    public void finalize() {
        super.finalize();
        int x = 2;
    }
}
        