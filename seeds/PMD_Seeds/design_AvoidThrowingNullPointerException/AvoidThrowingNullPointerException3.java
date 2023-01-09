
public class Foo {
    void foo() {
        Exception e = new NullPointerException();
        e.printStackTrace();
    }

    void bar() {
        Exception e = new RuntimeException();
        throw e;
    }
}
        