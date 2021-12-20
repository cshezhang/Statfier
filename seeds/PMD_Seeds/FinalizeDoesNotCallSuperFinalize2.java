package iter0;

public class Foo {
    public void finalize() {
        try {
        } finally {
            super.finalize();
        }
    }
}
        