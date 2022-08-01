
public class Foo {
    private int a;
    void bar() {
        Object o = new FooAdapter() {
            public void bar(Event e) {
                a = e.GetInt();
            }
        };
    }
    int baz() {
        return a;
    }
}
        