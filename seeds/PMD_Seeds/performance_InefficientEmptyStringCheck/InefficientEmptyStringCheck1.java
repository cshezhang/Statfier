
public class Foo {
    void bar() {
        String foo = "foo";
        if (foo.trim().length() == 0) {
            // this is bad
        }
    }
}
        