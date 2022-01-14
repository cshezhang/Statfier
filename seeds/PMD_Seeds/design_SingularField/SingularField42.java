
public class Foo {
    void myMethod() {
        Object o = new Object() {
            private int x;

            int bar(int y) {
                x = y + 5;
                return x;
            }
        };
    }
    Object field = new Object() {
        private int x;

        int bar(int y) {
            x = y + 5;
            return x;
        }
    };
    private Object doubleSingular;
    Object doBar(final int y) {
        doubleSingular = new Object() {
            private int x;

            int bar() {
                x = y + 5;
                return x;
            }
        };
        return doubleSingular;
    }
}
            