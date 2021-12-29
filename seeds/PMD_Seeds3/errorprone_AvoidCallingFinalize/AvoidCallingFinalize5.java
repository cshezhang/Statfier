
public class Foo {
    void foo () {
        Foo myFoo = new Foo(new FooOtherInterface() {
            protected void finalize() {
                super.finalize();
            }
        });
    }
}
        