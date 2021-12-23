
public class Foo {
    void foo(int bar) {
        if (foo()) {
            for (int i=0; i < 10; i++) {
                doSomethingWith(i);
                i++; // not OK
            }
        }
    }
}
        