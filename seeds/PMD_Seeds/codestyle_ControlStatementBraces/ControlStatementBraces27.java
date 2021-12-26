
public class Foo {
    void foo() {
        int x = 0;
        switch (x) {
        case 1:
        case 2: {    // here
            x++;
        }
        break;       // not here
        default: {   // not here
            break;
        }
        }
    }
}
        