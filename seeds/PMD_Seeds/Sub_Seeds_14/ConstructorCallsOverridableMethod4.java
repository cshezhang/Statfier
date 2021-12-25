
public class Foo {
    public Foo() {
        this("Bar");
    }
    private Foo(String bar) {
        bar();
    }
    public void bar() {}
}
        