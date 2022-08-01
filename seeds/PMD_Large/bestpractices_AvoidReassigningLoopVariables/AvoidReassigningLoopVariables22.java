
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            doSomethingWith(i);
            do doSomething(); while(foo(i++));
        }
    }
}
        