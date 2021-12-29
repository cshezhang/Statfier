
public class Foo {
    public void foo() {
        Object x;
        x = new Object();
        bar(x);
        x = null; // This is bad
    }
}
        