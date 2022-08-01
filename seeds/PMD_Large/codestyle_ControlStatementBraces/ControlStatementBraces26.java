
public class Foo {
    void foo() {
        int x = 0;
        switch (x) {
        case 1:
        case 2:    // here
            x++;   // not here
            break; // not here
        default:   // here
            break; // not here
        }
    }
}
        