
public class Foo {
    void foo() {
        int x = 0;
        switch (x) {
        case 1:
        case 2: {    // here, should have a single block
            x++;
        }
        {
            break;
        }
        default: {   // not here
            break;
        }
        }
    }
}
        