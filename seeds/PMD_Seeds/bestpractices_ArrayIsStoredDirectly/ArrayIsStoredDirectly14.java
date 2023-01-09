
public class Foo {
    private final int[] a;

    public Foo(int[] b) {
        this.a = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            this.a[i] = b[i];
        }
    }
}
        