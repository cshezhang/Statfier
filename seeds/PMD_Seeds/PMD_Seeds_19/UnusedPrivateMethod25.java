
public class Foo {
    private void endTest(final SourceLocation sourceLocation, final String message, final Object... params) {
    }
    public static void main(String[] args) {
        Foo f = new Foo();
        f.endTest(sourceLocation, "", (Object[]) null);
        f.endTest(sourceLocation, format, params);
    }
}
        