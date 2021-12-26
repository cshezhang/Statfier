
public class Foo {
    void bar(String s) {
        Exception e = new RuntimeException();
        if (s.equals("throw")) {
            throw e;
        }
        e = new NullPointerException();
    }
}
        