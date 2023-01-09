
public class Foo {
    public void bar() {
        Thread t = new Thread();
        t.run();                // use t.start() instead
    }
}
        