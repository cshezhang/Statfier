
public class Foo {
    private class Bar {
        private int size;

        private Bar() {
        }

        void bar() {
            new Bar[size];
        }
    }
}
        