
public class Foo {
    private String foo;
    void baz() {
        Runnable r = new Runnable() {
            public void run() {
                String foo = "buz";
            }
        };
    }
}
        