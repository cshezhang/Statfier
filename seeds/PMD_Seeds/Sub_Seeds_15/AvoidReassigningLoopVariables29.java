
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            doSomethingWith(i);
            if (foo()) i += 1;
            if (foo()) i = 5;
        }
    }
}
        