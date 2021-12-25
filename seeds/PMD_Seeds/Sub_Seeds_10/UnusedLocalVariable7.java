
public class Foo {
    public void foo() {
        final int x = 2;
        new Runnable() {
            public void run() {
                System.out.println(x);
            }
        };
    }
}
        