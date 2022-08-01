
public class Foo {
    void bar() {
        if (a.equals(baz.foo()) && baz != null) {} // baz could be null, misplaced null check
        if (a.equals(baz.foo()) || baz == null) {} // baz could be null, misplaced null check

        if (a.equals(baz.foo()) && null != baz) {} // baz could be null, misplaced null check
        if (a.equals(baz.foo()) || null == baz) {} // baz could be null, misplaced null check

        if (baz != null && a.equals(baz.foo())) {} // correct null check
        if (baz == null || a.equals(baz.foo())) {} // correct null check
    }
}
        