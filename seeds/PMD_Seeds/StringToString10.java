package iter0;

public class Foo {
    public String foo() {
        return "Test String";
    }

    public int bar() {
        String test = this.foo().toString();
        return test.length();
    }
}
        