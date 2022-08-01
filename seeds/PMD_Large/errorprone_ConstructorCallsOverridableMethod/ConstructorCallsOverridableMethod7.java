
public class Foo {
    public Foo(String s) {
        addActionListener(() -> bar());
        addActionListener(() -> { bar(); });
        addActionListener((a) -> { bar(); });
        addActionListener(this::bar);
    }
    public void bar() {}
}
        