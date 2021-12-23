
public class Foo {
    void bar(Object x) {
        if (x instanceof String && ((String)x).substring(2) != null) {}
    }
}
        