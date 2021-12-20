package iter0;

public class Foo {
    public void foo() {
        assert isRoot() ? parentContext == null : parentContext != null;
    }
}
        