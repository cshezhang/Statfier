
public class Foo {
    void bar() {
        if (a.equals(baz) && a != null) {} // a could be null, misplaced null check
        if (a.equals(baz) || a == null) {} // a could be null, misplaced null check

        if (a.equals(baz) && null != a) {} // a could be null, misplaced null check
        if (a.equals(baz) || null == a) {} // a could be null, misplaced null check

        if (a != null && a.equals(baz)) {} // correct null check
        if (a == null || a.equals(baz)) {} // correct null check
    }
}
        