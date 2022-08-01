
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            if (foo())
                doSomethingWith(i++);
            else if (bar())
                doSomethingElseWith(i--);
        }
    }
}
        