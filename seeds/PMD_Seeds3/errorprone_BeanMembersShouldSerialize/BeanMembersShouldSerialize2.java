
public class Foo {
    private transient String foo;
    private String bar = Foo.foo;
}
        