
public class Foo {
    private Foo fooField;

    public void bar() {
        Foo f = new Foo();
        f.fooField.buz();
    }
    private void buz() {}
}
        