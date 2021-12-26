
public class Foo {
    void bar() {
        try {
            foo();
        } catch (Exception e) {
            if (e instanceof FooException) {}
        }
    }
}
        