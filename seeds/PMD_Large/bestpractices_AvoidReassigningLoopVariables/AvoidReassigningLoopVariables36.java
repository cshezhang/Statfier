
public class Foo {
    void foo(int bar) {
        String[] strings = getStrings();
        for(String s : strings) {
            s = s.trim();
            doSomethingWith(s);
        }
    }
}
        