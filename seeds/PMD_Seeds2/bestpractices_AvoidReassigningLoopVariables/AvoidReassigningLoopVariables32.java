
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            doSomethingWith(i);
            i++; // not OK
            i--; // not OK
            ++i; // not OK
            --i; // not OK
            i += 1; // not OK
            i -= 1; // not OK
            i *= 1; // not OK
            i /= 1; // not OK
            i = 5; // not OK
        }
    }
}
        