
public class Foo {
    void foo(int bar) {
        for (int i=0; i < 10; i++) {
            doSomethingWith(i);
            for (int j=0; j < foo(); j++) i++;
        }
    }
}
        