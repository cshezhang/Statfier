
public class Foo {
    void bar() {
        int captured = 0;
        int shadowed = 2;
        new Foo(shadowed = 4) {
            int f = captured;
            {
                f = 2;
            }
        };
    }
}
        