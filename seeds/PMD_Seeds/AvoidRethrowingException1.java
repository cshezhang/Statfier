package iter0;

public class Foo {
    void bar() {
        try {
        } catch (SomeException se) {
            System.out.println("something interesting");
            throw se;
        }
    }
}
        