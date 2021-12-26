
public class Foo {
    void bar() {
        int captured = 0;
        int shadowed = 2;
        class Local {
            int f = captured;
            Local(int shadowed) {
                f = shadowed;
            }
        }
    }
}
        