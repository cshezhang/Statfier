package iter0;

public class Foo {
    public void finalize() {
        try {
        } catch(Exception e) {
        } finally {
            super.finalize();
        }
    }
}
        