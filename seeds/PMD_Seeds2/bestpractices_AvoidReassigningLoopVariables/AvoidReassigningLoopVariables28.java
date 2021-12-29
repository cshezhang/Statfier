
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            i++;
            if (foo()) continue;
            doSomethingWith(i);
        }
    }
}
        