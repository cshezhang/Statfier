
public class Foo {
    void foo(int bar) {
        String[] strings = getStrings();
        for(String s : strings) {
            doSomethingWith(s);
            s = s.toUpper(); // not OK
            doSomethingElseWith(s);
        }
    }
}
        