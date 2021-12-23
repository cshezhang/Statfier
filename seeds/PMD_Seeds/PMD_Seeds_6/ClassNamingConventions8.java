
public class Foo {

    private Foo() {
    }

    public Foo() {
        // This constructor shouldn't exist,
        // but we consider the class a utility class anyway
    }

    static final int ZERO = 0;


    static int bar() {
        return bar();
    }

}
        