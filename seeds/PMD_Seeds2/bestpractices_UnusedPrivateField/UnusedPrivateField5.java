
public class Foo {
    private static final String FOO = "foo";
    public Runnable bar() {
        return new Runnable() {
            public void run() {
                FOO.toString();
            }
        };
    }
}
        