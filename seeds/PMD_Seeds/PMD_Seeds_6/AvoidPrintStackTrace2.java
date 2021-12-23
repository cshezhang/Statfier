
public class Foo {
    void bar() {
        try {
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }
    }
}
        