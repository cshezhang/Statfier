package iter0;

public class Foo {
    public void finalize() {
        int x = 2;
        super.finalize();
    }
}
        