
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            doSomethingWith(i);
            if (foo()) {
                i++;
                i--;
                ++i;
                --i;
                i += 1;
                i -= 1;
                doSomethingWith(i++);
                i *= 1; // not OK
                i /= 1; // not OK
                i = 5; // not OK
                doSomethingWith(i = 5); // not OK
            }
        }
    }
}
        