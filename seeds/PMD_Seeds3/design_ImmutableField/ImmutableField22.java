
public class Foo {
    private static class Bar {
        private int i;

        Bar(final int i) {
            this.i = i;
        }
    }

    public int foo() {
        final Bar b = new Bar(1);
        b.i = 0;
        return b.i;
    }
}
        