package iter0;

public class Foo {
    public void bar() {
        new Runnable() {
            public void run() {
                foo();
            }
        };
    }
    private void foo() {}
}
        