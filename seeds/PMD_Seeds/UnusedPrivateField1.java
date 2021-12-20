package iter0;

public class Foo {
    private String foo;
    private String bar = foo;
    void buz() {
        bar = bar + 1;
    }
}
        